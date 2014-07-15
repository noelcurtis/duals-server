package backend.services

import java.util.UUID
import backend.models.User

trait UserDao {

  def create(user: User)
  def delete(id:UUID)
  def update(user: User)
  def findByEmail(email: String) : Option[User]
  def findById(id: UUID) : Option[User]

}
