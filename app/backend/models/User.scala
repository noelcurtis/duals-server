package backend.models

import java.util.UUID

/**
 * Represents a User, unique id is email/id
 * @param id
 * @param email
 * @param password
 * @param firstName
 * @param lastName
 */
case class User(id: UUID, email: String, password: String,
                    firstName: Option[String] = None,
                    lastName: Option[String] = None) {

}

object User {
  val ID_FIELD = "id"
  val EMAIL_FIELD = "email"
  val PASSWORD_FIELD = "password"
  val FIRST_NAME_FIELD = "first_name"
  val LAST_NAME_FIELD = "last_name"
}
