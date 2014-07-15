package backend.service

import backend.models.{Ladder}
import java.util.UUID

trait LadderDao {

  def create(ladder: Ladder)
  def delete(id:UUID)
  def update(ladder: Ladder)
  def findById(id: UUID) : List[Ladder]
  def fineByIdSorted(id: UUID) : List[Ladder]

}
