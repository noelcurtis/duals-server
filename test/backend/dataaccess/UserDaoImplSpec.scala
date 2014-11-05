package backend.dataaccess

import backend.SpecificationHelper
import backend.model.User
import com.datastax.driver.core.utils.UUIDs
import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._


@RunWith(classOf[JUnitRunner])
class UserDaoImplSpec extends Specification {
  val subject = new UserDaoImpl(SpecificationHelper.connection)

  step(SpecificationHelper.truncate(UserDaoImpl.tableName))

  "UserDaoImplSpec" should {

    "Create new user and find user successfully" in {
      val newUser = SpecificationHelper.generateUser(1)
      val createdUser = subject.create(newUser)

      createdUser.isDefined shouldEqual (true)

      val found = subject.findByEmail(newUser.email)
      found match {
        case Some(user) => {
          user.email shouldEqual newUser.email
          user.password shouldEqual newUser.password
          user.authToken.isDefined shouldEqual true
          user.updateTime.isDefined shouldEqual true
          success
        }
        case _ => failure("user not found by email")
      }

      val foundId = subject.findById(newUser.id)
      foundId match {
        case Some(user) => {
          user.email shouldEqual newUser.email
          user.password shouldEqual newUser.password
          user.authToken.isDefined shouldEqual true
          user.updateTime.isDefined shouldEqual true
          success
        }
        case _ => failure("user not found by id")
      }
    }

    "Delete a user successfully" in {
      val newUser = SpecificationHelper.generateUser(2)
      subject.create(newUser)

      subject.delete(newUser.email)
      val found = subject.findByEmail(newUser.email)
      found match {
        case Some(user) => {
          failure("user not deleted")
        }
        case _ => success
      }
    }

    "Update a user successfully" in {
      val newUser = SpecificationHelper.generateUser(3)
      subject.create(newUser)

      subject.update(User(id = newUser.id,
                          email = newUser.email,
                          password = newUser.password,
                          firstName = Option("another"),
                          lastName = Option("name"),
                          authToken = Option(UUIDs.random().toString),
                          authTokenUpdateTime = Option(new DateTime())))
      val found = subject.findByEmail(newUser.email)
      found match {
        case Some(user) => {
          user.email shouldEqual newUser.email
          user.id shouldEqual newUser.id
          user.password shouldEqual newUser.password
          user.firstName.get shouldEqual "another"
          user.lastName.get shouldEqual "name"
          user.updateTime.isDefined shouldEqual(true)
          user.authToken.get shouldNotEqual null
          user.authTokenUpdateTime.isDefined shouldEqual true
          success
        }
        case _ => failure("user not updated")
      }
    }

    "Create a duplicate user fails" in {
      val newUser = SpecificationHelper.generateUser(4)
      subject.create(newUser)
      // create the same user again
      val notCreated = subject.create(newUser)

      notCreated.isDefined mustEqual (false)
    }

    "Does not find a non existent User by email" in {
      val foundUser = subject.findByEmail("blah@gmail.com")

      foundUser.isDefined mustEqual(false)
    }

    "Does not find a non existent User by Id" in {
      val foundUser = subject.findById(UUIDs.random())

      foundUser.isDefined mustEqual(false)
    }


  }

  step(SpecificationHelper.truncate(UserDaoImpl.tableName))

}
