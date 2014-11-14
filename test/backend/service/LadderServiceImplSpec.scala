package backend.service

import java.util.UUID

import backend.dataaccess.{UserLadderDao, UserDao}
import backend.model.{LadderCreateParameters, Ladder, UserLadder, User}
import com.datastax.driver.core.utils.UUIDs
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LadderServiceImplSpec extends Specification with Mockito {

  // mock the user dao
  val userDao = mock[UserDao]
  // mock the ladder dao
  val userLadderDao = mock[UserLadderDao]

  val testUserId = UUIDs.random()

  // a test user
  val testUserWithAuthToken = Option(User(id = testUserId, email = "foo@bar.com",
    password = "something", firstName = Option("hello"), lastName = Option("bob"),
    authToken = Option(UUIDs.random().toString), authTokenUpdateTime = Option(new DateTime())))

  userDao.findById(testUserId).returns(testUserWithAuthToken)

  val ladderService = new LadderServiceImpl(userDao = userDao, userLadderDao = userLadderDao)

  "Ladder Service " should {

    "Create a ladder for a user successfully" in {

      val params = LadderCreateParameters(name = "testLadder", activity = "table tennis")

      val createdLadderId = ladderService.createLadderForUser(params, testUserId)

      createdLadderId.isDefined.shouldEqual(true)

    }


    "Create a ladder for a user with invalid parameters fails" in {

      val params = LadderCreateParameters(name = "", activity = "")

      val createdLadderId = ladderService.createLadderForUser(params, testUserId)

      createdLadderId.isDefined.shouldEqual(false)

    }

  }

}
