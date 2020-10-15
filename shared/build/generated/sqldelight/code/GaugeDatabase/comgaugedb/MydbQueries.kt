package comgaugedb

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface MydbQueries : Transacter {
  fun <T : Any> selectSession(mapper: (trackedAt: Long, Id: Long) -> T): Query<T>

  fun selectSession(): Query<SessionInfo>

  fun <T : Any> filterEventOnSessionId(sessionId: Long, mapper: (
    eventName: String,
    eventType: String,
    attributes: String?,
    trackedAt: Long,
    sessionId: Long
  ) -> T): Query<T>

  fun filterEventOnSessionId(sessionId: Long): Query<Events>

  fun <T : Any> selectAll(mapper: (
    eventName: String,
    eventType: String,
    attributes: String?,
    trackedAt: Long,
    sessionId: Long
  ) -> T): Query<T>

  fun selectAll(): Query<Events>

  fun insertSession(trackedAt: Long, Id: Long?)

  fun insertEvent(
    eventName: String,
    eventType: String,
    attributes: String?,
    trackedAt: Long,
    sessionId: Long
  )

  fun deleteAll()
}
