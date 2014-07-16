package backend.service

import java.util.UUID

import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.{Cluster, Session}
import backend.models.User

object SpecificationHelper {
  val contactPoint = "127.0.0.1"
  val keySpace = "test_clash"
  val email = "foo@bar.com"
  val password = "foobar"

  lazy val connection = Cluster.builder().addContactPoint(contactPoint).build().connect(keySpace)

  def close = {
    connection.close()
  }

  def truncate(table: String) = {
    connection.execute(QueryBuilder.truncate(table))
  }

  def generateUser(counter: Integer) : User = {
    val user = User(id = UUID.randomUUID(),
      email = counter + email,
      password = counter + password,
      firstName = Option(counter + "foo"),
      lastName = Option(counter + "bar"))
    user
  }
}
