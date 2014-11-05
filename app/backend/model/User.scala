package backend.model

import java.util.UUID

import org.joda.time.DateTime

/**
 * Represents a User, unique id is email/id
 * @param id
 * @param email
 * @param password
 * @param firstName
 * @param lastName
 * @param authToken
 */
case class User(id: UUID, email: String, password: String,
                    firstName: Option[String] = None,
                    lastName: Option[String] = None,
                    authToken: Option[String] = None,
                    authTokenUpdateTime: Option[DateTime] = None,
                    updateTime: Option[DateTime] = None) {

}

object User {
  val ID_FIELD = "id"
  val EMAIL_FIELD = "email"
  val PASSWORD_FIELD = "password"
  val FIRST_NAME_FIELD = "first_name"
  val LAST_NAME_FIELD = "last_name"
  val AUTH_TOKEN_FIELD = "auth_token"
  val AUTH_TOKEN_UPDATE_TIME_FIELD = "auth_token_update_time"
  val UPDATE_TIME_FIELD = "update_time"
}
