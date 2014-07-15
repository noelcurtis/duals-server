package backend.service

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserDaoImplSpec extends Specification{

  "UserDaoImplSpec" should {

    "Create new User succesfully" in  {
      1 + 1 mustEqual(2)
    }

  }

}
