package backend.service

import backend.models.{MatchStatus, MatchResult}
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

    "Can delete a match succesfully" in {
      val newMatch = SpecificationHelper.generateNewMatch()
      subject.create(newMatch)
      subject.delete(newMatch.id)
      // check that the user is deleted
      subject.findByUserId(newMatch.firstParticipant).size shouldEqual(0)
    }

  }

  step(SpecificationHelper.truncate(MatchDaoImpl.tableName))
}
