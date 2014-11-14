package controllers

import backend.dataaccess.{UserLadderDaoImpl, UserDaoImpl}
import backend.service.{LadderServiceImpl, UserServiceImpl}
import com.datastax.driver.core.Cluster
import play.api.mvc.{Action, Controller}

object Ladder extends Controller {

  // TODO: lets get this injected with DI please
  lazy val connection = Cluster.builder().addContactPoint("127.0.0.1").build().connect("clash")
  lazy val userDao = new UserDaoImpl(connection)
  lazy val userLadderDao = new UserLadderDaoImpl(connection)
  lazy val userService = new UserServiceImpl(userDao)
  lazy val ladderService = new LadderServiceImpl(userDao, userLadderDao)

  def create = Action(parse.json) { request =>
    Ok("hello")
  }

}
