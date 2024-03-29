package backend.dataaccess

import java.util.UUID

import backend.SpecificationHelper
import backend.model.{MatchStatus, MatchResult}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class MatchDaoImplSpec extends Specification {
  val subject = new MatchDaoImpl(SpecificationHelper.connection)

  step(SpecificationHelper.truncate(MatchDaoImpl.tableName))

  "MatchDaoImpl" should {

    "Can create a match successfully" in {
      val newMatch = SpecificationHelper.generateNewMatch()
      subject.create(newMatch)

      val foundFirstUserMatches = subject.findByUserId(newMatch.firstParticipant)
      // check that the participants were added correctly
      foundFirstUserMatches(0).firstParticipant shouldEqual(newMatch.firstParticipant)
      foundFirstUserMatches(0).secondParticipant shouldEqual(newMatch.secondParticipant)
      foundFirstUserMatches(0).result shouldEqual(MatchResult.None)
      foundFirstUserMatches(0).status shouldEqual(MatchStatus.OPEN)
      foundFirstUserMatches(0).scheduled shouldEqual(newMatch.scheduled)

      val foundSecondUserMatches = subject.findByUserId(newMatch.secondParticipant)
      // check that the participants were added correctly
      foundSecondUserMatches(0).firstParticipant shouldEqual(newMatch.secondParticipant)
      foundSecondUserMatches(0).secondParticipant shouldEqual(newMatch.firstParticipant)
      foundSecondUserMatches(0).result shouldEqual(MatchResult.None)
      foundSecondUserMatches(0).status shouldEqual(MatchStatus.OPEN)
      foundSecondUserMatches(0).scheduled shouldEqual(newMatch.scheduled)
    }

    step(SpecificationHelper.truncate(MatchDaoImpl.tableName))

    "Can delete a match successfully" in {
      val newMatch = SpecificationHelper.generateNewMatch()
      subject.create(newMatch)
      subject.delete(newMatch.id, newMatch.ladderId)
      // check that the user is deleted
      subject.findByUserId(newMatch.firstParticipant).size shouldEqual(0)
    }

    step(SpecificationHelper.truncate(MatchDaoImpl.tableName))

    "Can get all matches for a user successfully" in {
      val matches = SpecificationHelper.generateMatches()
      subject.create(matches)

      val foundUserMatches = subject.findByUserId(UUID.fromString(SpecificationHelper.testUserId))
      // check all the matches are present
      foundUserMatches.size shouldEqual(10)
    }

  }

  step(SpecificationHelper.truncate(MatchDaoImpl.tableName))
}
