package backend.models

import java.util.{Date, UUID}
import backend.models.MatchResult.MatchResult
import backend.models.MatchStatus.MatchStatus

/**
 * Match is used as an entry between 2 players on a ladder, a match is
 * uniquely identified by its Id and Ladder Id
 * @param id
 * @param ladderId the id for the Ladder that this match is on
 * @param firstParticipant
 * @param secondParticipant
 * @param result
 * @param created the date when this match was first created
 * @param scheduled the date when this match is scheduled for
 * @param status indicates the status of the Match (OPEN, ACCEPTED, COMPLETE)
 */
case class Match (id: UUID, ladderId: UUID, firstParticipant: UUID, secondParticipant: UUID,
                  result: MatchResult = MatchResult.None, created: Date = new Date(), scheduled: Date,
                  status: MatchStatus = MatchStatus.OPEN) {

  /**
   * Creates a Match with first and second participant swapped,
   * this is required because 2 entries are made for each match,
   * the second is made by swapping the participants this is so that
   * matches can be searched for both users
   * @return
   */
  def swapFirstSecond() : Match = {
    Match(id = this.id, ladderId = this.ladderId,
      firstParticipant = this.secondParticipant,
      secondParticipant = this.firstParticipant,
      result = swapMatchResult(this.result),
      created = this.created,
      scheduled = this.scheduled,
      status = MatchStatus.OPEN
    )
  }

  private def swapMatchResult(result: MatchResult) : MatchResult = {
    result match {
      case MatchResult.FIRST_PARTICIPANT => MatchResult.SECOND_PARTICIPANT
      case MatchResult.SECOND_PARTICIPANT => MatchResult.FIRST_PARTICIPANT
      case _ => result
    }
  }

}

object Match {
  val ID_FIELD = "id"
  val LADDER_ID_FIELD = "ladder_id"
  val FIRST_PARTICIPANT = "first_participant"
  val SECOND_PARTICIPANT = "second_participant"
  val RESULT = "result"
  val CREATED = "created"
  val SCHEDULED = "scheduled"
  val STATUS = "status"
}

object MatchResult extends Enumeration {
  type MatchResult = Value
  val None = Value(1) // the match is undecided
  val FIRST_PARTICIPANT = Value(2) // the first participant won
  val SECOND_PARTICIPANT = Value(3) // the second participant won
  val DRAW = Value(4) // the match is a draw
}

object MatchStatus extends Enumeration {
  type MatchStatus = Value
  val OPEN  = Value(1) // match created but not accepted
  val ACCEPTED = Value(2) // match accepted
  val COMPLETED = Value(3) // match completed
}