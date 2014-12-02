package backend.model

import java.util.UUID

case class UserLadder(userId: UUID,
                      ladderId: UUID,
                      creator: Boolean = false,
                      scheduledMatches: Int = 0,
                      wins: Int = 0,
                      rank: Int = -1) {

  def isUnranked = {
    rank == -1
  }

}

object UserLadder {
  var USER_ID_FIELD = "user_id"
  var LADDER_ID_FIELD = "ladder_id"
  var CREATOR_FIELD = "creator"
  var SCHEDULED_MATCHES_FIELD = "scheduled_matches"
  var WINS_FIELD = "wins"
  var RANK_FIELD = "rank"
}