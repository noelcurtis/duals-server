package backend.model

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

object ModelSerializer {

  implicit val userAsJsonWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "email" -> user.email,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "authToken" -> user.authToken,
      "authTokenUpdateTime" -> user.updateTime.map(f => f.getMillis),
      "updateTime" -> user.updateTime.map(f => f.getMillis)
    )
  }

  implicit val userAsJsonReads: Reads[User] = (
    (JsPath \ "id").read[UUID] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "authToken").readNullable[String] and
      (JsPath \ "authTokenUpdateTime").readNullable[DateTime] and
      (JsPath \ "updateTime").readNullable[DateTime]
  )(User.apply _)

  implicit val authenticationParametersJsonReads: Reads[AuthenticationParameters] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String]
  )(AuthenticationParameters.apply _)

  implicit val userCreateDetailsJsonReads : Reads[UserCreateParameters] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String]
  )(UserCreateParameters.apply _)

}
