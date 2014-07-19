package backend.service

import java.util.{Date, UUID}

import backend.models.{Match, MatchResult, MatchStatus}
import com.datastax.driver.core.querybuilder.{Insert, QueryBuilder}
import com.datastax.driver.core.{BatchStatement, ConsistencyLevel, Row, Session}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class MatchDaoImpl(session: Session) extends MatchDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  private def createInsertQuery(`match`: Match): Insert = {
    val query = QueryBuilder.insertInto(MatchDaoImpl.tableName).
      value(Match.ID_FIELD, `match`.id).
      value(Match.CREATED, new Date()).
      value(Match.FIRST_PARTICIPANT, `match`.firstParticipant).
      value(Match.SECOND_PARTICIPANT, `match`.secondParticipant).
      value(Match.SCHEDULED, `match`.scheduled).
      value(Match.RESULT, MatchResult.None.id).
      value(Match.STATUS, MatchStatus.OPEN.id).ifNotExists()
    query
  }

  override def create(`match`: Match) = {
    val batch = new BatchStatement()
    batch.add(createInsertQuery(`match`))
    batch.add(createInsertQuery(`match`.swapFirstSecond()))
    session.execute(batch)
  }

  override def findByUserId(id: UUID): List[Match] = {
    val rows = session.execute(
      QueryBuilder.select().
        from(MatchDaoImpl.tableName).
        where(QueryBuilder.eq(Match.FIRST_PARTICIPANT, id))
    ).all()
    rows.map(x => rowToMatch(x)).toList
  }

  override def delete(id: UUID) = {
    val query = QueryBuilder.delete().from(MatchDaoImpl.tableName).
      where(QueryBuilder.eq(Match.ID_FIELD, id)).
      setConsistencyLevel(ConsistencyLevel.ONE)
    session.execute(query)
  }

  private def rowToMatch(row: Row): Match = {
    val result = Match(
      id = row.getUUID(Match.ID_FIELD),
      firstParticipant = row.getUUID(Match.FIRST_PARTICIPANT),
      secondParticipant = row.getUUID(Match.SECOND_PARTICIPANT),
      result = MatchResult(row.getInt(Match.RESULT)),
      created = row.getDate(Match.CREATED),
      scheduled = row.getDate(Match.SCHEDULED),
      status = MatchStatus(row.getInt(Match.STATUS))
    )
    result
  }
}

object MatchDaoImpl {
  val tableName = "match"
}
