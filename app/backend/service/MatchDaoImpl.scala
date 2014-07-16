package backend.service

import java.util.{Date, UUID}
import com.datastax.driver.core.querybuilder.QueryBuilder
import backend.models.{MatchStatus, Result, Match}
import com.datastax.driver.core.{Row, ConsistencyLevel, Session}
import org.slf4j.LoggerFactory

class MatchDaoImpl(session: Session) extends MatchDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)
  val tableName = "match"

  override def create(`match`: Match): Unit = {
    val query = QueryBuilder.insertInto(tableName).
      value(Match.ID_FIELD, `match`.id).
      value(Match.CREATED, new Date()).
      value(Match.FIRST_PARTICIPANT, `match`.firstParticipant).
      value(Match.SECOND_PARTICIPANT, `match`.secondParticipant).
      value(Match.SCHEDULED, `match`.scheduled).
      value(Match.RESULT, `match`.result.id).
      value(Match.STATUS, `match`.status).ifNotExists()
    val resultSet = session.execute(query)
    resultSet.one().getBool(0) match {
      case true => logger.info("Created User {}", `match`.toString)
      case _ => throw new RuntimeException("Error creating Match " + `match`.toString)
    }
  }

  override def update(`match`: Match): Unit = {
    val query = QueryBuilder.update(tableName).
      `with`(QueryBuilder.set(Match.ID_FIELD, `match`.id)).
      and(QueryBuilder.set(Match.CREATED, `match`.created)).
      and(QueryBuilder.set(Match.FIRST_PARTICIPANT, `match`.firstParticipant)).
      and(QueryBuilder.set(Match.SECOND_PARTICIPANT, `match`.secondParticipant)).
      and(QueryBuilder.set(Match.SCHEDULED, `match`.scheduled)).
      and(QueryBuilder.set(Match.RESULT, `match`.result.id)).
      and(QueryBuilder.set(Match.STATUS, `match`.status)).
      where(QueryBuilder.eq(Match.ID_FIELD, `match`.id))
    val resultSet = session.execute(query)
    resultSet.one().getBool(0) match {
      case true => logger.info("Updated Match with id {}", `match`.id)
      case _ => throw new RuntimeException("Error updating Match with id " + `match`.id)
    }
  }

  override def findById(id: UUID): Option[Match] = {
    val query = QueryBuilder.select().from(tableName).where(QueryBuilder.eq(Match.ID_FIELD, id)).limit(1)
    val row = Some(session.execute(query).one())
    row match {
      case Some(row) => Some(rowToMatch(row))
      case _ => None
    }
  }

  override def delete(id: UUID): Unit = {
    val query = QueryBuilder.delete().from(tableName).
      where(QueryBuilder.eq(Match.ID_FIELD, id)).
      setConsistencyLevel(ConsistencyLevel.ONE)
    val resultSet = session.execute(query)
    resultSet.one().getBool(0) match {
      case true => logger.info("Deleted Match with id {}", id)
      case _ => throw new RuntimeException("Error deleting Match with id " + id)
    }
  }

  private def rowToMatch(row: Row) : Match = {
    val result = Match(
      id = row.getUUID(Match.ID_FIELD),
      firstParticipant = row.getUUID(Match.FIRST_PARTICIPANT),
      secondParticipant = row.getUUID(Match.SECOND_PARTICIPANT),
      result = Result(row.getInt(Match.RESULT)),
      created = row.getDate(Match.CREATED),
      scheduled = row.getDate(Match.SCHEDULED),
      status = MatchStatus(row.getInt(Match.STATUS))
    )
    result
  }
}
