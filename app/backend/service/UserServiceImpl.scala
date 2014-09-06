package backend.service

import backend.dataaccess.UserDao
import backend.model.User
import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import org.slf4j.LoggerFactory

class UserServiceImpl(userDao: UserDao) extends UserService {

  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def signUpUser(user: User): Option[User] = {
    //TODO: Check that the email address is valid
    val saltyUser = user.copy(password = UserServiceImpl.hashAndSaltPassword(user.password))
    userDao.create(saltyUser)
  }

  override def authenticateUser(email: String, password: String): Option[User] = {
    val foundUser = userDao.findByEmail(email)
    foundUser match {
      case Some(user) => {
        if (user.password.equals(UserServiceImpl.hashAndSaltPassword(password))) {
          foundUser
        } else {
          logger.debug(s"Authentication failed for user with id ${user.id}")
          None
        }
      }
      case _ => {
        logger.debug(s"User not found for email ${email}")
        None
      }
    }
  }

}

object UserServiceImpl {

  val hashFunction = Hashing.sha512()
  val salt = "something really complicated"

  def hashAndSaltPassword(password: String): String = {
    val hashCode = hashFunction.newHasher()
      .putString(password, Charsets.UTF_8)
      .putString(salt, Charsets.UTF_8)
      .hash()
    hashCode.toString
  }
  
}
