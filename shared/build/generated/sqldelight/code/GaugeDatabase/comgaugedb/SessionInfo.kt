package comgaugedb

import kotlin.Long
import kotlin.String

data class SessionInfo(
  val trackedAt: Long,
  val Id: Long
) {
  override fun toString(): String = """
  |SessionInfo [
  |  trackedAt: $trackedAt
  |  Id: $Id
  |]
  """.trimMargin()
}
