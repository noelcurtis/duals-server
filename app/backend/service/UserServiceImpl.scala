package backend.service

import backend.dataaccess.UserDao
import backend.model.{AuthenticationParameters, User, UserCreateParameters}
import com.datastax.driver.core.utils.UUIDs
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

class UserServiceImpl(userDao: UserDao) extends UserService {

  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def signUpUser(user: UserCreateParameters): Option[User] = {
    if (UserServiceImpl.isEmailValid(user.email)) {
      // create a user with parameters and auth token
      val saltyUser = User(id = UUIDs.random,
        email = user.email,
        password = UserServiceImpl.hashAndSaltPassword(user.password),
        firstName = Option(user.firstName),
        lastName = Option(user.lastName),
        authToken = Option(UUIDs.random().toString),
        authTokenUpdateTime = Option(new DateTime()))

      try {
        userDao.create(saltyUser)
      } catch {
        case e: Exception => logger.error("Error creating user", e); None
      }

    } else {
      None
    }
  }

  override def authenticateUser(authParameters: AuthenticationParameters): Option[User] = {
    val foundUser = userDao.findByEmail(authParameters.email)
    foundUser match {
      case Some(user) => {
        // authenticate the user
        if (user.password.equals(UserServiceImpl.hashAndSaltPassword(authParameters.password))) {
          // generate a new token for the user and update the user
          val updatedUser = user.copy(authToken = Option(UUIDs.random().toString), authTokenUpdateTime = Option(new DateTime()))

          try {
            userDao.update(updatedUser)
          } catch {
            case e: Exception => logger.error("Error updating user", e); None
          }

          Option(updatedUser)
        } else {
          logger.info(s"Authentication failed for user with id ${user.id}")
          None
        }
      }
      case _ => {
        logger.info(s"User not found for email ${authParameters.email}")
        None
      }
    }
  }

  override def checkAuthTokenValidity(authToken: String): Boolean = {
    val foundUser = userDao.findByAuthToken(authToken)
    foundUser match {
      case Some(user) => {
        if (user.authTokenUpdateTime.isDefined &&
          user.authTokenUpdateTime.get.isAfter(new DateTime().minusDays(UserServiceImpl.authTokenValidDays))) {
          true
        } else {
          logger.info(s"Auth token is not valid for user with id ${user.id}")
          false
        }
      }
      case _ => {
        logger.info(s"User not found for auth token ${authToken}")
        false
      }
    }
  }

}

object UserServiceImpl {

  val authTokenValidDays = 30;
  val hashFunction = Hashing.sha512()
  val salt = "something really complicated"
  val emailRegex = """(\w+)@([\w\.]+)""".r

  def hashAndSaltPassword(password: String): String = {
    val hashCode = hashFunction.newHasher()
      .putString(password, Charsets.UTF_8)
      .putString(salt, Charsets.UTF_8)
      .hash()
    hashCode.toString
  }

  def isEmailValid(email: String): Boolean = {
    return email.matches(emailRegex.toString())
  }

}
