package controllers

import backend.dataaccess.{UserLadderDaoImpl, UserDaoImpl}
import backend.model.{LadderCreateParameters, BadlyFormattedRequest, UnauthorizedAccess}
import backend.service.{LadderServiceImpl, UserServiceImpl}
import com.datastax.driver.core.Cluster
import controllers.User._
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import backend.model.ModelSerializer._

object Ladder extends Controller {

  // TODO: lets get this injected with DI please
  lazy val connection = Cluster.builder().addContactPoint("127.0.0.1").build().connect("clash")
  lazy val userDao = new UserDaoImpl(connection)
  lazy val userLadderDao = new UserLadderDaoImpl(connection)
  lazy val userService = new UserServiceImpl(userDao)
  lazy val ladderService = new LadderServiceImpl(userDao, userLadderDao)

  def create = Action(parse.json) { request =>
    val authorization = request.headers.get(HeaderNames.AUTHORIZATION)
    authorization match {
      case Some(token) => {

        val foundUser = userService.checkAuthTokenValidity(token)
        foundUser match {
          case Some(user) => {

            val checkParameters = request.body.validate[LadderCreateParameters]
            checkParameters.fold(
              errors => {
                // Badly formatted request parameters
                BadRequest(BadlyFormattedRequest().renderJson())
              },
              params => {
                // create the ladder
                ladderService.createLadderForUser(params, user.id)
                Ok
              }
            )

          }
          case _ => Unauthorized(UnauthorizedAccess().renderJson())
        }

      }
      case _ => Unauthorized(UnauthorizedAccess().renderJson())
    }
  }

  def laddersForUser = Action { request =>
    val authorization = request.headers.get(HeaderNames.AUTHORIZATION)
    authorization match {
      case Some(token) => {

        val foundUser = userService.checkAuthTokenValidity(token)
        foundUser match {
          case Some(user) => {
            // get all the ladders for the user
            Ok(Json.toJson(ladderService.findLaddersForUser(user.id)))

          }
          case _ => Unauthorized(UnauthorizedAccess().renderJson())
        }

      }
      case _ => Unauthorized(UnauthorizedAccess().renderJson())
    }
  }

}
