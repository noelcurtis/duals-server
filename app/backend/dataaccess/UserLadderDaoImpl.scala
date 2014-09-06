package backend.dataaccess

import java.util.UUID

import backend.model.UserLadder
import com.datastax.driver.core.{Row, Session}
import com.datastax.driver.core.querybuilder.{Insert, QueryBuilder}
import org.slf4j.LoggerFactory

class UserLadderDaoImpl(session: Session) extends UserLadderDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  private def createInsertQuery(userLadder: UserLadder): Insert = {
    val query = QueryBuilder.insertInto(UserLadderDaoImpl.tableName).
                value(UserLadder.USER_ID_FIELD, userLadder.userId).
                value(UserLadder.LADDER_ID_FIELD, userLadder.ladderId).
                value(UserLadder.CREATOR_FIELD, userLadder.creator).ifNotExists()
    query
  }

  /**
   * Use to create an entry for UserLadder
   * @param userLadder
   */
  override def create(userLadder: UserLadder) {
    session.execute(createInsertQuery(userLadder))
  }

  /**
   * Use to delete a UserLadder by userId and ladderId
   * @param userId
   * @param ladderId
   */
  override def delete(userId: UUID, ladderId: UUID) {
    val query = QueryBuilder.delete().from(UserLadderDaoImpl.tableName).
                where(QueryBuilder.eq(UserLadder.USER_ID_FIELD, userId)).
                and(QueryBuilder.eq(UserLadder.LADDER_ID_FIELD, ladderId))
    session.execute(query)
  }

  /**
   * Use to find a UserLadder by userId and ladderId
   * @param userId
   * @param ladderId
   */
  override def find(userId: UUID, ladderId: UUID): Option[UserLadder] = {
    val query = QueryBuilder.select().from(UserLadderDaoImpl.tableName).
      where(QueryBuilder.eq(UserLadder.USER_ID_FIELD, userId)).
      and(QueryBuilder.eq(UserLadder.LADDER_ID_FIELD, ladderId)).limit(1)
    val row = Option(session.execute(query).one())
    row match {
      case Some(row) => Option(rowToUserLadder(row))
      case _ => None
    }
  }

  private def rowToUserLadder(row: Row) = {
    val userLadder = UserLadder(
      userId = row.getUUID(UserLadder.USER_ID_FIELD),
      ladderId = row.getUUID(UserLadder.LADDER_ID_FIELD),
      creator = row.getBool(UserLadder.CREATOR_FIELD)
    )
    userLadder
  }

}

object UserLadderDaoImpl {
  val tableName = "user_ladder"
}
