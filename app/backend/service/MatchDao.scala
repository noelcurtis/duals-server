package backend.service

import backend.models.{Match, User}
import java.util.UUID

trait MatchDao {

  def create(`match`: Match)
  def delete(id:UUID)
  def findByUserId(id: UUID) : List[Match]

}
