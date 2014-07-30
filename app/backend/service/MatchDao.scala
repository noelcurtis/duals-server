package backend.service

import backend.models.{Match, User}
import java.util.UUID

trait MatchDao {

  /**
   * Use to create a Match
   * @param `match`
   */
  def create(`match`: Match)

  /**
   * Use to create a Collection of Matches
   * @param matches
   */
  def create(matches: List[Match])

  /**
   * Use to delete a Match, by Match Id and Ladder Id
   * @param id
   * @param ladderId
   */
  def delete(id:UUID, ladderId: UUID)

  /**
   * Find a match by User Id
   * @param id
   * @return
   */
  def findByUserId(id: UUID) : List[Match]

}
