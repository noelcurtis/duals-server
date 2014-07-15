package backend.service

import backend.models.{Match, Ladder, User}
import java.util.UUID

trait LadderService {

  /**
   * Use to create a Ladder by a User
   * @param user
   */
  def createALadder(user: User)

  /**
   * Use to create a match between 2 users on a Ladder
   * @param challenger
   * @param challenged
   * @param ladder
   */
  def createAMatch(challenger: User, challenged: User, ladder: Ladder)

  /**
   * Use to mark a Winner on a match, the Match will also be marked
   * Completed and Ladder rankings will be updated
   * @param `match`
   * @param winner
   */
  def winAMatch(`match`: Match, winner: User)

  /**
   * Get results on a Ladder in sorted order of Users with highest winnings
   * @param id
   * @return
   */
  def getSortedLadderResults(id: UUID) : List[Ladder]

}
