package backend.dataaccess

import java.util.UUID

import backend.model.{Ladder, UserLadder}
import com.datastax.driver.core._
import com.datastax.driver.core.querybuilder.{QueryBuilder}
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

class UserLadderDaoImpl(session: Session) extends UserLadderDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def create(userLadder: UserLadder, ladder: Ladder) {
    session.execute(createCombinedInsert(userLadder, ladder))
  }

  override def delete(userId: UUID, ladderId: UUID) {
    val query = QueryBuilder.delete().from(UserLadderDaoImpl.USER_LADDER_TABLE_NAME).
      where(QueryBuilder.eq(UserLadder.USER_ID_FIELD, userId)).
      and(QueryBuilder.eq(UserLadder.LADDER_ID_FIELD, ladderId))
    session.execute(query)
  }

  override def findUserLadder(userId: UUID, ladderId: UUID): Option[UserLadder] = {
    val query = QueryBuilder.select().from(UserLadderDaoImpl.USER_LADDER_TABLE_NAME).
      where(QueryBuilder.eq(UserLadder.USER_ID_FIELD, userId)).
      and(QueryBuilder.eq(UserLadder.LADDER_ID_FIELD, ladderId)).limit(1)
    val row = Option(session.execute(query).one())
    row match {
      case Some(row) => Option(rowToUserLadder(row))
      case _ => None
    }
  }

  override def findUserLadders(userId: UUID): List[UserLadder] = {
    val query = QueryBuilder.select().from(UserLadderDaoImpl.USER_LADDER_TABLE_NAME)
      .where(QueryBuilder.eq(UserLadder.USER_ID_FIELD, userId))
    val rows = session.execute(query).all().asScala
    val userLadders = rows.map(f => rowToUserLadder(f)).toList
    userLadders
  }

  override def findLadder(ladderId: UUID): Option[Ladder] = {
    val query = QueryBuilder.select().from(Ladder.TABLE_NAME)
      .where(QueryBuilder.eq(Ladder.LADDER_ID_FIELD, ladderId))
      .limit(1)
    val row = Option(session.execute(query).one())
    row match {
      case Some(row) => Option(rowToLadder(row))
      case _ => None
    }
  }

  override def create(userLadder: UserLadder): Unit = ???

  override def delete(userLadder: UserLadder): Unit = ???

  override def findLadders(ladderIdList: List[UUID]): List[Ladder] = ???

  // helper methods //

  private def createUserLadderInsertQuery(userLadder: UserLadder): Statement = {
    val query = QueryBuilder.insertInto(UserLadderDaoImpl.USER_LADDER_TABLE_NAME).
      value(UserLadder.USER_ID_FIELD, userLadder.userId).
      value(UserLadder.LADDER_ID_FIELD, userLadder.ladderId).
      value(UserLadder.NAME_FIELD, userLadder.name).
      value(UserLadder.ACTIVITY_FIELD, userLadder.activity).
      value(UserLadder.CREATOR_FIELD, userLadder.creator).
      value(UserLadder.SCHEDULED_MATCHES_FIELD, userLadder.scheduledMatches).
      value(UserLadder.WINS_FIELD, userLadder.wins).
      value(UserLadder.RANK_FIELD, userLadder.rank).
      ifNotExists().
      setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
    query
  }

  private def createLadderInsertQuery(ladder: Ladder): Statement = {
    val query = QueryBuilder.insertInto(UserLadderDaoImpl.LADDER_TABLE_NAME)
      .value(Ladder.LADDER_ID_FIELD, ladder.ladderId)
      .value(Ladder.NAME_FIELD, ladder.name)
      .value(Ladder.ACTIVITY_FIELD, ladder.activity)
      .value(Ladder.CREATE_TIME_FIELD, ladder.createTime.toDate)
      .ifNotExists()
      .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
    query
  }

  private def createCombinedInsert(userLadder: UserLadder, ladder: Ladder): Statement = {
    val batch = new BatchStatement()
      .add(createUserLadderInsertQuery(userLadder))
      .add(createLadderInsertQuery(ladder))
    batch
  }

  private def rowToUserLadder(row: Row) = {
    val userLadder = UserLadder(
      userId = row.getUUID(UserLadder.USER_ID_FIELD),
      ladderId = row.getUUID(UserLadder.LADDER_ID_FIELD),
      name = row.getString(UserLadder.NAME_FIELD),
      activity = row.getString(UserLadder.ACTIVITY_FIELD),
      creator = row.getBool(UserLadder.CREATOR_FIELD),
      scheduledMatches = row.getInt(UserLadder.SCHEDULED_MATCHES_FIELD),
      wins = row.getInt(UserLadder.WINS_FIELD),
      rank = row.getInt(UserLadder.RANK_FIELD)
    )
    userLadder
  }

  private def rowToLadder(row: Row) = {
    val ladder = Ladder(
      ladderId = row.getUUID(Ladder.LADDER_ID_FIELD),
      name = row.getString(Ladder.NAME_FIELD),
      activity = row.getString(Ladder.ACTIVITY_FIELD),
      createTime = new DateTime(row.getDate(Ladder.CREATE_TIME_FIELD))
    )
    ladder
  }

  // helper methods //
}

object UserLadderDaoImpl {
  val USER_LADDER_TABLE_NAME = "user_ladder"
  val LADDER_TABLE_NAME = "ladder"
}
