package backend.service

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserLadderDaoSpec extends Specification {
  val subject = new UserLadderDaoImpl(SpecificationHelper.connection)

  step(SpecificationHelper.truncate(UserDaoImpl.tableName))

  "UserLadderDao" should {

    "Create new user-ladder successfully" in {
      val newUserLadder = SpecificationHelper.generateUserLadder()
      subject.create(newUserLadder)

      val found = subject.find(newUserLadder.userId, newUserLadder.ladderId)
      found match {
        case Some(userLadder) => {
          userLadder shouldEqual newUserLadder
          success
        }
        case _ => failure("user-ladder not found by user-id and ladder-id")
      }
    }

    // create test for finding multiple ladders for a user

  }

}
