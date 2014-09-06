package backend.service

import backend.SpecificationHelper
import backend.dataaccess.UserDao
import backend.model.User
import com.datastax.driver.core.utils.UUIDs
import org.junit.runner.RunWith
import org.specs2.mock._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserServiceImplSpec extends Specification with Mockito {

  // mock the user dao
  val userDao = mock[UserDao]
  // create a test user
  val testUser = User(id = UUIDs.random(), email = "foo@bar.com", password = "something")
  // mock a successful user creation
  userDao.create(testUser.copy(password = UserServiceImpl.hashAndSaltPassword(testUser.password)))
    .returns(Option(testUser.copy(password = UserServiceImpl.hashAndSaltPassword(testUser.password))))
  // create another test user
  val failTestUser = SpecificationHelper.generateUser(2)
  // mock a failed user creation
  userDao.create(failTestUser.copy(password = UserServiceImpl.hashAndSaltPassword(failTestUser.password))) returns None
  // mock finding a user with email
  userDao.findByEmail(testUser.email).returns(Option(testUser.copy(password = UserServiceImpl.hashAndSaltPassword(testUser.password))))
  // mock not finding a user with email
  userDao.findByEmail(failTestUser.email) returns None

  val subject = new UserServiceImpl(userDao)

  "UserService" should {

    "Be able to sign up a user successfully" in {
      val signedUp = subject.signUpUser(testUser)

      signedUp.isDefined.shouldNotEqual(false)
      signedUp.get.shouldEqual(testUser.copy(password = UserServiceImpl.hashAndSaltPassword(testUser.password)))
    }

    "Be able to recover from not signing up a user successfully" in {
      val notSignedUp = subject.signUpUser(failTestUser)

      notSignedUp.isDefined.shouldEqual(false)
      notSignedUp.shouldEqual(None)
    }

    "Be able to authenticate a user" in {
      val authedUser = subject.authenticateUser(testUser.email, testUser.password)

      authedUser.isDefined.shouldEqual(true)
      authedUser.get.shouldEqual(testUser.copy(password = UserServiceImpl.hashAndSaltPassword(testUser.password)))
    }

    "Be able to detect a user with email that does not exist" in {
      val authedUser = subject.authenticateUser(failTestUser.email, failTestUser.password)

      authedUser.isDefined.shouldEqual(false)
      authedUser.shouldEqual(None)
    }

    "Be able to detech a mismatched password" in {
      val authedUser = subject.authenticateUser(testUser.email, "bad password")

      authedUser.isDefined.shouldEqual(false)
      authedUser.shouldEqual(None)
    }

  }
}
