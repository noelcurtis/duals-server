package scaldi

import backend.dataaccess._
import backend.service.{LadderService, LadderServiceImpl, UserService, UserServiceImpl}
import com.datastax.driver.core.Session

class BackendModule extends Module {

  // setup all the bindings
  bind[Session] to Global.session
  bind[MatchDao] to injected[MatchDaoImpl]
  bind[UserDao] to injected[UserDaoImpl]
  bind[UserLadderDao] to injected[UserLadderDaoImpl]
  bind[UserService] to injected[UserServiceImpl]
  bind[LadderService] to injected[LadderServiceImpl]

}
