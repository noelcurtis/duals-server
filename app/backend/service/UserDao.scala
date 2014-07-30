package backend.service

import java.util.UUID
import backend.models.User

trait UserDao {

  /**
   * Use to create a User
   * @param user
   */
  def create(user: User)

  /**
   * Use to delete a User by email
   * @param email
   */
  def delete(email:String)

  /**
   * Use to update a User
   * @param user
   */
  def update(user: User)

  /**
   * Use to find a User by email
   * @param email
   * @return
   */
  def findByEmail(email: String) : Option[User]

  /**
   * Use to find a User by id
   * @param id
   * @return
   */
  def findById(id: UUID) : Option[User]

}
