package backend.service

import backend.model.User

trait UserService {

  /**
   * Use to sign a User up, assumes that the User has an assigned auth token
   * @param user
   * @return
   */
  def signUpUser(user: User): Option[User]

  /**
   * Use to check if a User with email exists and check if password matches that user
   * @param email
   * @param password
   * @return
   */
  def authenticateUser(email: String, password: String): Option[User]

}
