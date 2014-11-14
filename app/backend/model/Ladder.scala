package backend.model

import java.util.UUID

import org.joda.time.DateTime

case class Ladder(ladderId: UUID, name: String, activity: String, createTime: DateTime) {

}

object Ladder {
  var TABLE_NAME = "ladder"
  var LADDER_ID_FIELD = "ladder_id"
  var NAME_FIELD = "name"
  var ACTIVITY_FIELD = "activity"
  var CREATE_TIME_FIELD = "create_time"
}
