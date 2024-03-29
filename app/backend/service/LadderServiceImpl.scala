package backend.service

import java.util.UUID

import backend.dataaccess.{UserDao, UserLadderDao}
import backend.model.{Ladder, UserLadder, LadderCreateParameters}
import com.datastax.driver.core.utils.UUIDs
import org.joda.time.DateTime

class LadderServiceImpl(userDao: UserDao, userLadderDao: UserLadderDao) extends LadderService {

  override def createLadderForUser(parameters: LadderCreateParameters, userId: UUID): Option[UUID] = {
    // validate that the user exists just in case
    val foundUser = userDao.findById(userId)

    foundUser match {
      case Some(user) => {
        // validate the ladder create parameters
        if (LadderServiceImpl.validateLadderCreateParameters(parameters)) {

          // create a uuid for the ladder
          val ladderId = UUIDs.timeBased();
          // Create a user ladder and user
          val userLadder = new UserLadder(
            userId = userId,
            ladderId = ladderId,
            name = parameters.name,
            activity = parameters.activity,
            creator = true
          )
          val ladder = new Ladder(
            ladderId = ladderId,
            activity = parameters.activity,
            name = parameters.name,
            createTime = new DateTime()
          )

          userLadderDao.create(userLadder, ladder)
          Option(ladderId)

        } else {
          None // invalid ladder parameters
        }
      }
      case _ => None // user does not exist
    }

  }

  override def findLaddersForUser(userId: UUID): List[UserLadder] = {
    userLadderDao.findUserLadders(userId)
  }

}

object LadderServiceImpl {

  def validateLadderCreateParameters(parameters: LadderCreateParameters) = {
    if (parameters.activity.equalsIgnoreCase("") || parameters.name.equalsIgnoreCase("")) {
      false
    } else {
      true
    }
  }

}
