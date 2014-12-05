package backend

import java.util.{Date, UUID}

import backend.model.{Ladder, Match, User, UserLadder}
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer

class SpecificationHelper {

}

object SpecificationHelper {

  val contactPoint = "127.0.0.1"
  val keySpace = "test_clash"
  val email = "foo@bar.com"
  val password = "foobar"
  val testUserId = "5f2a5f9f-d7c3-4fa4-b0d1-c904a15a7781"
  val secondTestUserId = "e8abfa6d-31c3-4ddf-944f-844f75c1bbc1"
  val testLadderId = "cf1feaea-1523-4f18-b42b-0f98a449a3d7"

  lazy val connection = Cluster.builder().addContactPoint(contactPoint).build().connect(keySpace)

  def close = {
    connection.close()
  }

  def truncate(table: String) = {
    connection.execute(QueryBuilder.truncate(table))
  }

  def generateUser(counter: Integer): User = {
    val user = User(id = UUID.randomUUID(),
      email = counter + email,
      password = counter + password,
      firstName = Option(counter + "foo"),
      lastName = Option(counter + "bar"))
    user
  }

  /**
   * Generates one match associated with testUserId
   * @return
   */
  def generateNewMatch(): Match = {
    val `match` = Match(id = UUIDs.timeBased(),
      ladderId = UUIDs.random(),
      firstParticipant = UUID.fromString(testUserId),
      secondParticipant = UUID.fromString(secondTestUserId),
      scheduled = new Date())
    `match`
  }

  /**
   * Generates 10 matches associated with testUserId
   * @return
   */
  def generateMatches(): List[Match] = {
    var matchList = new ListBuffer[Match]
    for (x <- 1 to 10) {
      matchList += Match(id = UUIDs.timeBased(),
        ladderId = UUIDs.random(),
        firstParticipant = UUID.fromString(testUserId),
        secondParticipant = UUID.randomUUID(),
        scheduled = new Date())
    }
    matchList.toList
  }

  /**
   * Generates a Ladder and UserLadder both associated with the test user
   * @return
   */
  def generateUserLadderAndLadder(): (UserLadder, Ladder) = {
    (generateUserLadder(), generateLadder())
  }

  def generateLadder(): Ladder = {
    val ladder = Ladder(ladderId = UUID.fromString(testLadderId),
      name = "A Test Ladder",
      activity = "Table Tennis",
      createTime = new DateTime())
    return ladder;
  }

  /**
   * Generates a user-ladder
   * @return
   */
  def generateUserLadder(): UserLadder = {
    val userLadder = UserLadder(userId = UUID.fromString(testUserId),
      ladderId = UUID.fromString(testLadderId),
      creator = true,
      name = "A Test Ladder",
      activity = "Table Tennis"
    )
    userLadder
  }

  /**
   * Generates a List of UserLadder
   * @return
   */
  def generateUserLadders(): List[UserLadder] = {
    var userLadderList = new ListBuffer[UserLadder]
    for (x <- 1 to 10) {
      userLadderList += UserLadder(userId = UUID.fromString(testUserId),
        ladderId = UUID.randomUUID(),
        creator = false,
        name = "A Test Ladder",
        activity = "Table Tennis"
      );
    }
    userLadderList.toList
  }
}
