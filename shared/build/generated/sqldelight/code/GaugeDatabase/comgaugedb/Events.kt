package comgaugedb

import kotlin.Long
import kotlin.String

data class Events(
  val eventName: String,
  val eventType: String,
  val attributes: String?,
  val trackedAt: Long,
  val sessionId: Long
) {
  override fun toString(): String = """
  |Events [
  |  eventName: $eventName
  |  eventType: $eventType
  |  attributes: $attributes
  |  trackedAt: $trackedAt
  |  sessionId: $sessionId
  |]
  """.trimMargin()
}
