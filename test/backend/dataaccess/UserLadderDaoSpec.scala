package backend.dataaccess

import backend.SpecificationHelper
import backend.model.UserLadder
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserLadderDaoSpec extends Specification {
  val subject = new UserLadderDaoImpl(SpecificationHelper.connection)
  val cassandra = SpecificationHelper.connection

  step(SpecificationHelper.truncate(UserLadderDaoImpl.USER_LADDER_TABLE_NAME))
  step(SpecificationHelper.truncate(UserLadderDaoImpl.LADDER_TABLE_NAME))

  "UserLadderDao" should {

    "Create and find new UserLadder and Ladder successfully" in {
      val newUserLadder = SpecificationHelper.generateUserLadderAndLadder()
      subject.create(newUserLadder._1, newUserLadder._2)

      val foundLadder = subject.findLadder(newUserLadder._1.ladderId)
      foundLadder match {
        case Some(ladder) => {
          ladder.ladderId shouldEqual newUserLadder._2.ladderId
          ladder.name shouldEqual newUserLadder._2.name
          ladder.activity shouldEqual newUserLadder._2.activity
          ladder.createTime shouldNotEqual null
          success
        }
        case _ => failure("Ladder not found by ladder id")
      }

      val foundUserLadder = subject.findUserLadder(newUserLadder._1.userId, newUserLadder._1.ladderId)
      foundUserLadder match  {
        case Some(userLadder) => {
          userLadder.creator shouldEqual newUserLadder._1.creator
          userLadder.ladderId shouldEqual newUserLadder._1.ladderId
          userLadder.userId shouldEqual newUserLadder._1.userId
          success
        }
        case _ => failure("UserLadder not found by user-id and ladder-id")
      }
    }

    step(SpecificationHelper.truncate(UserLadderDaoImpl.USER_LADDER_TABLE_NAME))
    step(SpecificationHelper.truncate(UserLadderDaoImpl.LADDER_TABLE_NAME))

    "Find all UserLadder for user" in {
      val newUserLadder = SpecificationHelper.generateUserLadderAndLadder()
      subject.create(newUserLadder._1, newUserLadder._2)

      val userLadders = subject.findUserLadders(newUserLadder._1.userId)
      userLadders.size shouldEqual 1
      userLadders(0).creator shouldEqual newUserLadder._1.creator
      userLadders(0).ladderId shouldEqual newUserLadder._1.ladderId
      userLadders(0).userId shouldEqual newUserLadder._1.userId
    }

    step(SpecificationHelper.truncate(UserLadderDaoImpl.USER_LADDER_TABLE_NAME))
    step(SpecificationHelper.truncate(UserLadderDaoImpl.LADDER_TABLE_NAME))

    "Create a UserLadder with invalid attributes fails" in {
      val newUserLadder = null
      try {
        subject.create(null, null)
        failure("Create UserLadder with null parameter succeeds")
      } catch {
        case _ => success
      }
    }


  }

  step(SpecificationHelper.truncate(UserLadderDaoImpl.USER_LADDER_TABLE_NAME))
  step(SpecificationHelper.truncate(UserLadderDaoImpl.LADDER_TABLE_NAME))

}
