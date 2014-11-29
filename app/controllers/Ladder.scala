package controllers

import backend.model.ModelSerializer._
import backend.model.{BadlyFormattedRequest, GenericError, LadderCreateParameters, UnauthorizedAccess}
import backend.service.{LadderService, UserService}
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class Ladder(userService: UserService, ladderService: LadderService) extends Controller {

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
                val ladderId = ladderService.createLadderForUser(params, user.id)
                ladderId match {
                  case Some(ladderId) => Ok(Json.obj("ladderId" -> ladderId.toString))
                  case _ => Ok(Json.toJson(GenericError().renderJson()))
                }

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
