package backend.model

import play.api.libs.json.{Json, JsValue}

trait ErrorCode {
  def getMessage(): String
  def getCode():Int

  def renderJson() : JsValue  = {
    Json.obj("error" -> getMessage(), "errorCode" -> getCode())
  }
}

case class BadlyFormattedRequest() extends ErrorCode {
  override def getMessage(): String = "Badly formatted request"
  override def getCode(): Int = 2
}

case class ErrorCreatingUserAccount() extends ErrorCode {
  override def getMessage(): String = "Error creating user account"
  override def getCode(): Int = 3
}

case class UnauthorizedAccess() extends ErrorCode {
  override def getMessage(): String = "Unauthorized access"
  override def getCode(): Int = 1
}

case class ExpiredToken() extends ErrorCode {
  override def getMessage(): String = "Expired token, re-authenticate"
  override def getCode(): Int = 4
}