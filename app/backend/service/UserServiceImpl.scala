package backend.service

import backend.dataaccess.UserDao
import backend.model.User
import org.slf4j.LoggerFactory

class UserServiceImpl(userDao: UserDao) extends UserService {

  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def signUpUser(user: User): Option[User] = {
    userDao.create(user)
  }

  override def authenticateUser(email: String, password: String): Option[User] = {
    val foundUser = userDao.findByEmail(email)
    foundUser match {
      case Some(user) => {
        if (user.password.equals(password)) {
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
