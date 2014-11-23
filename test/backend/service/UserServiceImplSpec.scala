package backend.service

import backend.dataaccess.UserDao
import backend.model.{AuthenticationParameters, User, UserCreateParameters}
import com.datastax.driver.core.utils.UUIDs
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.specs2.mock._
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserServiceImplSpec extends Specification with Mockito {

  // mock the user dao
  val userDao = mock[UserDao]
  // create some test user parameters
  val testUserParameters = UserCreateParameters(email = "foo@bar.com", password = "something", firstName = "hello", lastName = "bob")
  // mock a successful user creation
  val testUserWithAuthToken = Option(User(id = UUIDs.random(), email = "foo@bar.com",
    password = "something", firstName = Option("hello"), lastName = Option("bob"),
    authToken = Option(UUIDs.random().toString), authTokenUpdateTime = Option(new DateTime())))

  userDao.create(any[User]).returns(testUserWithAuthToken)

  val authParameters = AuthenticationParameters(email = "foo@bar.com", password = "something")
  // create a test user
  val testUser = User(id = UUIDs.random(), email = "foo@bar.com", password = "something")
  // mock finding a user with email
  userDao.findByEmail(testUser.email).returns(Option(testUser.copy(password = UserServiceImpl.hashAndSaltPassword(testUser.password))))
  // mock find by valid auth token
  userDao.findByAuthToken(testUserWithAuthToken.get.authToken.get).returns(testUserWithAuthToken)

  val testUserWithInvalidAuthToken = Option(User(id = UUIDs.random(), email = "foo@bar.com",
    password = "something", firstName = Option("hello"), lastName = Option("bob"),
    authToken = Option(UUIDs.random().toString), authTokenUpdateTime = Option(new DateTime().minusDays(100))))
  userDao.findByAuthToken(testUserWithInvalidAuthToken.get.authToken.get).returns(testUserWithInvalidAuthToken)

  val subject = new UserServiceImpl(userDao)

  "UserService" should {

    "Be able to sign up a user successfully" in {
      val signedUp = subject.signUpUser(testUserParameters)

      signedUp.isDefined.shouldNotEqual(false)
      signedUp.get.email.shouldEqual(testUserParameters.email)
      signedUp.get.firstName.shouldEqual(Option(testUserParameters.firstName))
      signedUp.get.lastName.shouldEqual(Option(testUserParameters.lastName))
      signedUp.get.authToken.isDefined.shouldEqual(true)
      signedUp.get.authTokenUpdateTime.isDefined.shouldEqual(true)
    }

    "Be able to authenticate a user" in {
      val authedUser = subject.authenticateUser(authParameters)

      authedUser.isDefined.shouldEqual(true)
      authedUser.get.email.shouldEqual(testUser.email)
      authedUser.get.authToken.isDefined.shouldEqual(true)
      authedUser.get.authTokenUpdateTime.isDefined.shouldEqual(true)
    }

    "Be able to detect a user with email that does not exist" in {
      val authedUser = subject.authenticateUser(AuthenticationParameters(email = "doesnotexist@bar.com", password = "bad password"))

      authedUser.isDefined.shouldEqual(false)
      authedUser.shouldEqual(None)
    }

    "Be able to detect a mismatched password" in {
      val authedUser = subject.authenticateUser(AuthenticationParameters(email = "foo@bar.com", password = "bad password"))

      authedUser.isDefined.shouldEqual(false)
      authedUser.shouldEqual(None)
    }

    "Be able to detect valid auth token" in {
      val valid = subject.checkAuthTokenValidity(testUserWithAuthToken.get.authToken.get)

      valid.isDefined shouldEqual(true)
    }

    "Be able to detech invalid auth token" in {
      val valid = subject.checkAuthTokenValidity(testUserWithInvalidAuthToken.get.authToken.get)

      valid.isDefined shouldEqual(false)
    }

  }
}
