package backend.dataaccess

import java.util.UUID

import backend.model.User
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.{ConsistencyLevel, Row, Session}
import org.slf4j.LoggerFactory

class UserDaoImpl(session: Session) extends UserDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def create(newUser: User) : Option[User] = {
    val user = newUser.copy(authToken = Option(UUIDs.random().toString))
    val query = QueryBuilder.insertInto(UserDaoImpl.tableName).
      value(User.ID_FIELD, user.id).
      value(User.EMAIL_FIELD, user.email).
      value(User.PASSWORD_FIELD, user.password).
      value(User.AUTH_TOKEN_FIELD, user.authToken.getOrElse(null)).
      value(User.FIRST_NAME_FIELD, user.firstName.getOrElse(null)).
      value(User.LAST_NAME_FIELD, user.lastName.getOrElse(null)).ifNotExists()
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
      where(QueryBuilder.eq(User.EMAIL_FIELD, user.email))
    session.execute(query)
  }

  override def findByEmail(email: String): Option[User] = {
    val query = QueryBuilder.select().from(UserDaoImpl.tableName).
      where(QueryBuilder.eq(User.EMAIL_FIELD, email)).
      limit(1)
    val row = Option(session.execute(query).one())
    row match {
      case Some(row) => Option(rowToUser(row))
      case _ => None
    }
  }

  override def findById(id: UUID): Option[User] = {
    val query = QueryBuilder.select().from(UserDaoImpl.tableName).
      where(QueryBuilder.eq(User.ID_FIELD, id)).
      limit(1).allowFiltering()
    val row = Option(session.execute(query).one())
    row match {
      case Some(row) => Option(rowToUser(row))
      case _ => None
    }
  }

  private def rowToUser(row: Row): User = {
    val user = User(
      id = row.getUUID(User.ID_FIELD),
      email = row.getString(User.EMAIL_FIELD),
      password = row.getString(User.PASSWORD_FIELD),
      firstName = Option(row.getString(User.FIRST_NAME_FIELD)),
      lastName = Option(row.getString(User.LAST_NAME_FIELD)),
      authToken = Option(row.getString(User.AUTH_TOKEN_FIELD))
    )
    user
  }
}

object UserDaoImpl {
  val tableName = "user"
}