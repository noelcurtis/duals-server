package backend.model

import java.util.{Date, UUID}
import backend.SpecificationHelper
import com.datastax.driver.core.utils.UUIDs
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MatchSpec extends Specification {

  "Match" should {

    "Can swap fields in a match" in {
      val newMatch = Match(id = UUIDs.timeBased(),
        ladderId = UUIDs.random(),
        firstParticipant = UUID.fromString(SpecificationHelper.testUserId),
        secondParticipant =  UUID.fromString(SpecificationHelper.secondTestUserId),
        scheduled = new Date())

      val swappedMatch = newMatch.swapFirstSecond()

      // check that participants are swapped correctly
      newMatch.firstParticipant shouldEqual(swappedMatch.secondParticipant)
      newMatch.secondParticipant shouldEqual(swappedMatch.firstParticipant)
      newMatch.result shouldEqual(MatchResult.None)
    }

    "Can swap MatchResults in a match" in {
      val newMatch = Match(id = UUIDs.timeBased(),
        ladderId = UUIDs.random(),
        firstParticipant = UUID.fromString(SpecificationHelper.testUserId),
        secondParticipant =  UUID.fromString(SpecificationHelper.secondTestUserId),
        result = MatchResult.FIRST_PARTICIPANT,
        scheduled = new Date())

      val swappedMatch = newMatch.swapFirstSecond()
      // check that participants are swapped correctly
      newMatch.firstParticipant shouldEqual(swappedMatch.secondParticipant)
      newMatch.secondParticipant shouldEqual(swappedMatch.firstParticipant)
      newMatch.result shouldEqual(MatchResult.FIRST_PARTICIPANT)
      swappedMatch.result shouldEqual(MatchResult.SECOND_PARTICIPANT)

    }

  }

}
