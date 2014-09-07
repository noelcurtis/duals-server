package backend.model

import java.util.UUID

case class UserLadder(userId: UUID, ladderId: UUID, creator: Boolean = false) {

}

object UserLadder {
  var USER_ID_FIELD = "user_id"
  var LADDER_ID_FIELD = "ladder_id"
  var CREATOR_FIELD = "creator"
}

// TODO(ncurtis): Update to create Ladder: with name, sport etc