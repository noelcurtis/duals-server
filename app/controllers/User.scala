package controllers

import backend.model.ModelSerializer._
import backend.model._
import backend.service.UserService
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

class User(userService: UserService) extends Controller {

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
