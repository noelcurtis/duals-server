package backend.service

import backend.model.{UserCreateParameters, AuthenticationParameters, User}

trait UserService {

  /**
   * Use to sign a User up
   * @param userCreateParameters
   * @return
   */
  def signUpUser(userCreateParameters: UserCreateParameters): Option[User]

  /**
   * Use to check if a User with email exists and check if password matches that user,
   * this will also update the auth token for the User
   * @return
   */
  def authenticateUser(authenticationParameters: AuthenticationParameters): Option[User]

  /**
   * Use to check the validity of an authentication token
   * @param authToken
   * @return
   */
  def checkAuthTokenValidity(authToken: String): Boolean

}
