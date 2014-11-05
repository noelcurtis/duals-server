package controllers

import backend.dataaccess.UserDaoImpl
import backend.model.ModelSerializer._
import backend.model.{UserCreateParameters, AuthenticationParameters, User}
import backend.service.UserServiceImpl
import com.datastax.driver.core.Cluster
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

object User extends Controller {

  // TODO: lets get this injected with DI please
  lazy val connection = Cluster.builder().addContactPoint("127.0.0.1").build().connect("clash")
  lazy val userDao = new UserDaoImpl(connection)
  lazy val userService = new UserServiceImpl(userDao)


  def authenticate() = Action(parse.json) { request =>
    val checkAuthParameters = request.body.validate[AuthenticationParameters]
    checkAuthParameters.fold(
      errors => {
        // Badly formatted request parameters
        BadRequest(getJsonError("Bad request format", 2))
      },
      authParameters => {
        val checkedUser = userService.authenticateUser(authParameters)
        checkedUser match {
          // Successful authentication
          case Some(user) => Ok(Json.toJson(user))
          // Unsuccessful authentication
          case _ => Unauthorized(getJsonError("Mismatched email or password", 1))
        }
      }
    )
  }

  def signUp() = Action(parse.json) { request =>
    val checkUser = request.body.validate[UserCreateParameters]
    checkUser.fold(
      errors => {
        // Badly formatted request parameters
        BadRequest(getJsonError("Bad request format", 2))
      },
      userToSignUp => {
        val checkedUser = userService.signUpUser(userToSignUp)
        checkedUser match {
          // Successful sign up
          case Some(user) => Ok(Json.toJson(user))
          // Unsuccessful sign up
          case _ => Unauthorized(getJsonError("Error creating account for user", 3))
        }
      }
    )
  }

  private def getJsonError(message: String, errorCode: Int): JsValue = {
    Json.obj("error" -> message, "errorCode" -> errorCode)
  }

}
