package backend.service

import java.util.UUID

import backend.model.{UserLadder, LadderCreateParameters}

trait LadderService {

  def createLadderForUser(parameters: LadderCreateParameters, userId: UUID) : Option[UUID]

  def findLaddersForUser(userId: UUID) : List[UserLadder]

  // should be able to add user to a ladder

  // should be able to add match on a ladder

  // should be able to get all matches for a ladder

}
