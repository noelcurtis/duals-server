package backend.service

import java.util.UUID

import backend.model.LadderCreateParameters

trait LadderService {

  def createLadderForUser(parameters: LadderCreateParameters, userId: UUID) : Option[UUID]

  // should be able to get all the ladders for a user

  // should be able to add user to a ladder

  // should be able to add match on a ladder

  // should be able to get all matches for a ladder

}
