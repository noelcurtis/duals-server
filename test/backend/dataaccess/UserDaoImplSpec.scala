package backend.dataaccess

import backend.SpecificationHelper
import backend.model.User
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

      createdUser.isDefined shouldEqual(true)

      val found = subject.findByEmail(newUser.email)
      found match {
        case Some(user) => {
          user shouldEqual newUser
          success
        }
        case _ => failure("user not found by email")
      }

      val foundId = subject.findById(newUser.id)
      foundId match {
        case Some(user) => {
          user shouldEqual newUser
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

      subject.update(User(newUser.id, newUser.email, newUser.password, Option("another"), Option("name")))
      val found = subject.findByEmail(newUser.email)
      found match {
        case Some(user) => {
          user.email shouldEqual newUser.email
          user.id shouldEqual newUser.id
          user.password shouldEqual newUser.password
          user.firstName.get shouldEqual "another"
          user.lastName.get shouldEqual "name"
          success
        }
        case _ => failure("user not updated")
      }
    }
  }

  step(SpecificationHelper.truncate(UserDaoImpl.tableName))

}
