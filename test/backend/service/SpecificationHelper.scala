package backend.service

import java.util.regex.MatchResult
import java.util.{Date, UUID}

import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.{Cluster, Session}
import backend.models.{MatchResult, User, Match}

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

  def generateNewMatch() : Match = {
    val `match` = Match(id = UUIDs.timeBased(),
      firstParticipant = UUID.fromString("5f2a5f9f-d7c3-4fa4-b0d1-c904a15a7781"),
      secondParticipant =  UUID.fromString("e8abfa6d-31c3-4ddf-944f-844f75c1bbc1"),
      scheduled = new Date())
    `match`
  }
}
