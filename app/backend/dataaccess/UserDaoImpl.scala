package backend.dataaccess

import java.util.{Date, UUID}

import backend.model.User
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.{ConsistencyLevel, Row, Session}
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

class UserDaoImpl(session: Session) extends UserDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def create(newUser: User): Option[User] = {
    val user = newUser.copy(
      authToken = Option(UUIDs.random().toString),
      authTokenUpdateTime = Option(new DateTime()),
      updateTime = Option(new DateTime())
    )

    val query = QueryBuilder.insertInto(UserDaoImpl.tableName).
      value(User.ID_FIELD, user.id).
      value(User.EMAIL_FIELD, user.email).
      value(User.PASSWORD_FIELD, user.password).
      value(User.AUTH_TOKEN_FIELD, user.authToken.getOrElse(null)).
      value(User.FIRST_NAME_FIELD, user.firstName.getOrElse(null)).
      value(User.LAST_NAME_FIELD, user.lastName.getOrElse(null)).
      value(User.AUTH_TOKEN_UPDATE_TIME_FIELD, user.authTokenUpdateTime.map(f => f.toDate).getOrElse(null)).
      value(User.UPDATE_TIME_FIELD, new Date())
      .ifNotExists().setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
    val resultSet = session.execute(query)

    resultSet.one().getBool(0) match {
      case true => {
        logger.info("Created user {}", user)
        Option(user)
      }
      case _ => {
        logger.error(s"Error creating User $user with $query")
        None
      }
    }
  }

  override def delete(email: String) = {
    val query = QueryBuilder.delete().from(UserDaoImpl.tableName).
      where(QueryBuilder.eq(User.EMAIL_FIELD, email)).
      setConsistencyLevel(ConsistencyLevel.ONE)
    session.execute(query)
  }

  override def update(user: User) = {
    val query = QueryBuilder.update(UserDaoImpl.tableName).
      `with`(QueryBuilder.set(User.PASSWORD_FIELD, user.password)).
      and(QueryBuilder.set(User.FIRST_NAME_FIELD, user.firstName.getOrElse(null))).
      and(QueryBuilder.set(User.LAST_NAME_FIELD, user.lastName.getOrElse(null))).
      and(QueryBuilder.set(User.AUTH_TOKEN_FIELD, user.authToken.getOrElse(null))).
      and(QueryBuilder.set(User.UPDATE_TIME_FIELD, new Date())).
      and(QueryBuilder.set(User.AUTH_TOKEN_UPDATE_TIME_FIELD, user.authTokenUpdateTime.map(f => f.toDate).getOrElse(null))).
      where(QueryBuilder.eq(User.EMAIL_FIELD, user.email)).setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)

    session.execute(query)
  }

  override def findByEmail(email: String): Option[User] = {
    if (!email.isEmpty) {
      val query = QueryBuilder.select().from(UserDaoImpl.tableName).
        where(QueryBuilder.eq(User.EMAIL_FIELD, email)).
        limit(1).setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)

      val row = Option(session.execute(query).one())

      row match {
        case Some(row) => Option(rowToUser(row))
        case _ => None
      }
    }
    None
  }

  override def findById(id: UUID): Option[User] = {
    val query = QueryBuilder.select().from(UserDaoImpl.tableName).
      where(QueryBuilder.eq(User.ID_FIELD, id)).
      limit(1).allowFiltering().setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)

    val row = Option(session.execute(query).one())

    row match {
      case Some(row) => Option(rowToUser(row))
      case _ => None
    }
  }

  override def findByAuthToken(authToken: String): Option[User] = {
    if (authToken != null) {
      val query = QueryBuilder.select().from(UserDaoImpl.tableName).
        where(QueryBuilder.eq(User.AUTH_TOKEN_FIELD, authToken)).
        limit(1).allowFiltering().setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)

      val row = Option(session.execute(query).one())

      row match {
        case Some(row) => Option(rowToUser(row))
        case _ => None
      }
    }
    None
  }

  private def rowToUser(row: Row): User = {
    var ut: Option[DateTime] = None
    if (row.getDate(User.UPDATE_TIME_FIELD) != null) {
      ut = Option(new DateTime(row.getDate(User.UPDATE_TIME_FIELD)))
    }

    var aut: Option[DateTime] = None
    if (row.getDate(User.AUTH_TOKEN_UPDATE_TIME_FIELD) != null) {
      aut = Option(new DateTime(row.getDate(User.AUTH_TOKEN_UPDATE_TIME_FIELD)))
    }

    val user = User(
      id = row.getUUID(User.ID_FIELD),
      email = row.getString(User.EMAIL_FIELD),
      password = row.getString(User.PASSWORD_FIELD),
      firstName = Option(row.getString(User.FIRST_NAME_FIELD)),
      lastName = Option(row.getString(User.LAST_NAME_FIELD)),
      authToken = Option(row.getString(User.AUTH_TOKEN_FIELD)),
      authTokenUpdateTime = aut,
      updateTime = ut
    )
    user
  }
}

object UserDaoImpl {
  val tableName = "user"
}