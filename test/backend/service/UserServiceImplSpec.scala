package backend.service

import backend.SpecificationHelper
import backend.dataaccess.UserDao
import org.junit.runner.RunWith
import org.specs2.mock._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserServiceImplSpec extends Specification with Mockito {

  // mock the user dao
  val userDao = mock[UserDao]
  // create a test user
  val testUser = SpecificationHelper.generateUser(1)
  // mock a successful user creation
  userDao.create(testUser) returns Option(testUser)
  // create another test user
  val failTestUser = SpecificationHelper.generateUser(2)
  // mock a failed user creation
  userDao.create(failTestUser) returns None

  // mock finding a user with email
  userDao.findByEmail(testUser.email) returns Option(testUser)

  // mock not finding a user with email
  userDao.findByEmail(failTestUser.email) returns None

  val subject = new UserServiceImpl(userDao)

  "UserService" should {

    "Be able to sign up a user successfully" in {
      val signedUp = subject.signUpUser(testUser)

      signedUp.isDefined.shouldNotEqual(false)
      signedUp.get.shouldEqual(testUser)
    }

    "Be able to recover from not signing up a user successfully" in {
      val notSignedUp = subject.signUpUser(failTestUser)

      notSignedUp.isDefined.shouldEqual(false)
      notSignedUp.shouldEqual(None)
    }

    "Be able to authenticate a user" in {
      val authedUser = subject.authenticateUser(testUser.email, testUser.password)

      authedUser.isDefined.shouldEqual(true)
      authedUser.get.shouldEqual(testUser)
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
