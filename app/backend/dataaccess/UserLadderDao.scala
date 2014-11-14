package backend.dataaccess

import java.util.UUID

import backend.model.{Ladder, UserLadder}

trait UserLadderDao {

  /**
   * Use to create a new Ladder and associate it to its creating User
   * @param userLadder
   * @param ladder
   */
  def create(userLadder: UserLadder, ladder: Ladder)

  /**
   * Use to create a new user ladder association, use to add a user to a ladder
   * @param userLadder
   */
  def create(userLadder: UserLadder)

  /**
   * Use to delete a user ladder association, use to remove a user from a ladder
   * @param userLadder
   */
  def delete(userLadder: UserLadder)

  /**
   * Use to delete a UserLadder by userId and ladderId, should remove association between user and ladder.
   * If the user is the owner of the ladder, should remove the ladder and all associations of user_ladder
   * @param userId
   * @param ladderId
   */
  def delete(userId: UUID, ladderId: UUID)

  /**
   * Use to find a UserLadder by userId and ladderId
   * @param userId
   * @param ladderId
   */
  def findUserLadder(userId: UUID, ladderId: UUID): Option[UserLadder]

  /**
   * Use to find all UserLadder for a user
   * @param userId
   * @return
   */
  def findUserLadders(userId: UUID): List[UserLadder]

  /**
   * Use to find a Ladder by id
   * @param ladderId
   * @return
   */
  def findLadder(ladderId: UUID): Option[Ladder]

  /**
   * Use to find all the ladders for the list of ladder ids
   * @param ladderIdList
   * @return
   */
  def findLadders(ladderIdList: List[UUID]): List[Ladder]


}
