package backend.models

import java.util.UUID

/**
 * Ladder used to indicate a ladder entry, isCreated indicates if userId created the Ladder,
 * the row key for the Ladder is (id, userId)
 * @param id
 * @param userId
 * @param name name of the ladder
 * @param firstName firstName of the User
 * @param lastName lastName of the User
 * @param wins how many wins the user has
 * @param losses how many losses the user has
 * @param isCreator indicates that the User with id userId created this ladder
 */
case class Ladder (id: UUID, userId: UUID, name: String, firstName: String,
                   lastName: String, wins: Integer = 0, losses: Integer = 0,
                   isCreator: Boolean = false) {

}

object Ladder {
  val ID_FIELD = "id"
  val USER_ID_FIELD = "user_id"
  val WINS_FIELD = "wins"
  val LOSSES_FIELD = "losses"
  val FIRST_NAME = "first_name"
  val LAST_NAME = "last_name"
  val NAME = "name"
}
