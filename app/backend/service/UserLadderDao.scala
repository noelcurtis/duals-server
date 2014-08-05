package backend.service

import java.util.UUID

import backend.models.UserLadder

trait UserLadderDao {

  /**
   * Use to create an entry for UserLadder
   * @param userLadder
   */
  def create(userLadder: UserLadder)

  /**
   * Use to delete a UserLadder by userId and ladderId
   * @param userId
   * @param ladderId
   */
  def delete(userId: UUID, ladderId: UUID)

  /**
   * Use to find a UserLadder by userId and ladderId
   * @param userId
   * @param ladderId
   */
  def find(userId: UUID, ladderId: UUID) : Option[UserLadder]

}
