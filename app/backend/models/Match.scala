package backend.models

import java.util.{Date, UUID}
import backend.models.MatchResult.Result
import backend.models.MatchStatus.MatchStatus

/**
 * Match is used as an entry between 2 players, the unique identifier for a match is id
 * @param id
 * @param firstParticipant
 * @param secondParticipant
 * @param result
 * @param created the date when this match was first created
 * @param scheduled the date when this match is scheduled for
 * @param status indicates the status of the Match (OPEN, ACCEPTED, COMPLETE)
 */
case class Match (id: UUID, firstParticipant: UUID, secondParticipant: UUID,
                  result: Result = MatchResult.None, created: Date = new Date(), scheduled: Date,
                  status: MatchStatus = MatchStatus.OPEN) {

  /**
   * Creates a Match with first and second participant swapped
   * @return
   */
  def swapFirstSecond() : Match = {
    Match(id = this.id,
      firstParticipant = this.secondParticipant,
      secondParticipant = this.firstParticipant,
      result = this.result,
      created = this.created,
      scheduled = this.scheduled,
      status = MatchStatus.OPEN
    )
  }

}

object Match {
  val ID_FIELD = "id"
  val FIRST_PARTICIPANT = "first_participant"
  val SECOND_PARTICIPANT = "second_participant"
  val RESULT = "result"
  val CREATED = "created"
  val SCHEDULED = "scheduled"
  val STATUS = "status"
}

object MatchResult extends Enumeration {
  type Result = Value
  val None = Value(1)
  val FIRST_PARTICIPANT = Value(2)
  val SECOND_PARTICOPANT = Value(3)
  val DRAW = Value(4)
}

object MatchStatus extends Enumeration {
  type MatchStatus = Value
  val OPEN  = Value(1) // match created but not accepted
  val ACCEPTED = Value(2) // match accepted
  val COMPLETED = Value(3) // match completed
}