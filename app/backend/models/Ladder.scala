package backend.models

import java.util.UUID

/**
 * Ladder used to indicate a ladder entry, isCreated indicates if userId created the Ladder,
 * the unique identifier for a Ladder is id + userId
 * @param id
 * @param userId
 * @param name
 * @param email
 * @param firstName
 * @param lastName
 * @param wins how many wins the user has
 * @param losses how many losses the user has
 * @param isCreated indicates that the user created this ladder
 */
case class Ladder (id: UUID, userId: UUID, name: String,  email: String,
                   firstName: String, lastName: String, wins: Integer = 0,
                   losses: Integer = 0, isCreated: Boolean = false) {

}

object Ladder {
  val ID_FIELD = "id"
  val USER_ID_FIELD = "userId"
  val WINS_FIELD = "winsField"
  val LOSSES_FIELD = "lossesField"
  val EMAIL = "email"
  val FIRST_NAME = "firstName"
  val LAST_NAME = "lastName"
  val NAME = "name"
}
