package controllers

import backend.dataaccess.UserDaoImpl
import backend.model.ModelSerializer._
import backend.model._
import backend.service.UserServiceImpl
import com.datastax.driver.core.Cluster
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

object User extends Controller {

  // TODO: lets get this injected with DI please
  lazy val connection = Cluster.builder().addContactPoint("127.0.0.1").build().connect("clash")
  lazy val userDao = new UserDaoImpl(connection)
  lazy val userService = new UserServiceImpl(userDao)

  /**
   * Use to authenticate a user
   * @return
   */
  def authenticate() = Action(parse.json) { request =>
    val checkAuthParameters = request.body.validate[AuthenticationParameters]
    checkAuthParameters.fold(
      errors => {
        // Badly formatted request parameters
        BadRequest(BadlyFormattedRequest().renderJson())
      },
      authParameters => {
        val checkedUser = userService.authenticateUser(authParameters)
        checkedUser match {
          // Successful authentication
          case Some(user) => Ok(Json.toJson(user))
          // Unsuccessful authentication
          case _ => Unauthorized(UnauthorizedAccess().renderJson())
        }
      }
    )
  }

  /**
   * Use to Sign up a new user
   * @return
   */
  def signUp() = Action(parse.json) { request =>
    val checkUser = request.body.validate[UserCreateParameters]
    checkUser.fold(
      errors => {
        // Badly formatted request parameters
        BadRequest(BadlyFormattedRequest().renderJson())
      },
      userToSignUp => {
        val checkedUser = userService.signUpUser(userToSignUp)
        checkedUser match {
          // Successful sign up
          case Some(user) => Ok(Json.toJson(user))
          // Unsuccessful sign up
          case _ => Ok(ErrorCreatingUserAccount().renderJson())
        }
      }
    )
  }

}
