package backend.model

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs

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
                    authToken: String = UUIDs.random().toString) {

}

object User {
  val ID_FIELD = "id"
  val EMAIL_FIELD = "email"
  val PASSWORD_FIELD = "password"
  val FIRST_NAME_FIELD = "first_name"
  val LAST_NAME_FIELD = "last_name"
  val AUTH_TOKEN_FIELD = "auth_token"
}
