package backend.services

import com.datastax.driver.core.{Row, ConsistencyLevel, Session}
import backend.models.User
import java.util.UUID
import com.datastax.driver.core.querybuilder.QueryBuilder
import org.slf4j.LoggerFactory

class UserDaoImpl(session: Session) extends UserDao {
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)
  val tableName = "user"

  override def create(user: User) = {
    val query = QueryBuilder.insertInto(tableName).
      value(User.ID_FIELD, UUID.randomUUID()).
      value(User.EMAIL_FIELD, user.email).
      value(User.PASSWORD_FIELD, user.password).
      value(User.FIRST_NAME_FIELD, user.firstName.getOrElse("")).
      value(User.LAST_NAME_FIELD, user.lastName.getOrElse("")).ifNotExists()
    val resultSet = session.execute(query)
    resultSet.one().getBool(0) match {
      case true => logger.info("Created User {}", user.toString)
      case _ => throw new RuntimeException("Error creating User " + user.toString)
    }
  }

  override def delete(id: UUID): Unit = {
    val query = QueryBuilder.delete().from(tableName).
      where(QueryBuilder.eq(User.ID_FIELD, id)).
      setConsistencyLevel(ConsistencyLevel.ONE)
    val resultSet = session.execute(query)
    resultSet.one().getBool(0) match {
      case true => logger.info("Deleted user with id {}", id)
      case _ => throw new RuntimeException("Error deleting User with id " + id)
    }
  }

  override def update(user: User) = {
    val query = QueryBuilder.update(tableName).
      `with`(QueryBuilder.set(User.EMAIL_FIELD, user.email)).
      and(QueryBuilder.set(User.PASSWORD_FIELD, user.password)).
      and(QueryBuilder.set(User.FIRST_NAME_FIELD, user.firstName.getOrElse(""))).
      and(QueryBuilder.set(User.LAST_NAME_FIELD, user.lastName.getOrElse(""))).
      where(QueryBuilder.eq(User.ID_FIELD, user.id))
    val resultSet = session.execute(query)
    resultSet.one().getBool(0) match {
      case true => logger.info("Updated user with id {}", user.id)
      case _ => throw new RuntimeException("Error updating User with id " + user.id)
    }
  }

  override def findByEmail(email: String): Option[User] = {
    val query = QueryBuilder.select().from(tableName).
      where(QueryBuilder.eq(User.EMAIL_FIELD, email)).
      limit(1)
    val row = Some(session.execute(query).one())
    row match {
      case None => None
      case Some(row) => Some(rowToUser(row))
    }
  }

  override def findById(id: UUID): Option[User] = {
    val query = QueryBuilder.select().from(tableName).
      where(QueryBuilder.eq(User.ID_FIELD, id)).
      limit(1)
    val row = Some(session.execute(query).one())
    row match {
      case None => None
      case Some(row) => Some(rowToUser(row))
    }
  }

  private def rowToUser(row: Row) : User = {
    val user = User(
      id = row.getUUID(User.ID_FIELD),
      email = row.getString(User.EMAIL_FIELD),
      password = row.getString(User.PASSWORD_FIELD),
      firstName = Some(row.getString(User.FIRST_NAME_FIELD)),
      lastName =  Some(row.getString(User.LAST_NAME_FIELD))
    )
    user
  }
}
