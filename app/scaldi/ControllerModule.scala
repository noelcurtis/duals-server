package scaldi

import controllers.{Application, Ladder, User}

class ControllerModule extends Module {
  binding to injected[User]
  binding to injected[Ladder]
  binding to new Application
}
