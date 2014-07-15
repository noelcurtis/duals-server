package backend.models

import java.util.{Date, UUID}
import backend.models.Result.Result
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
                  result: Result, created: Date, scheduled: Date, status: MatchStatus = MatchStatus.OPEN) {
}

object Match {
  val ID_FIELD = "id"
  val FIRST_PARTICIPANT = "firstParticipant"
  val SECOND_PARTICIPANT = "secondParticipant"
  val RESULT = "result"
  val CREATED = "created"
  val SCHEDULED = "scheduled"
  val STATUS = "status"
}

object Result extends Enumeration {
  type Result = Value
  val FIRST_PARTICIPANT = Value(1)
  val SECOND_PARTICOPANT = Value(2)
  val DRAW = Value(3)
}

object MatchStatus extends Enumeration {
  type MatchStatus = Value
  val OPEN  = Value(1) // match created but not accepted
  val ACCEPTED = Value(2) // match accepted
  val COMPLETED = Value(3) // match completed
}