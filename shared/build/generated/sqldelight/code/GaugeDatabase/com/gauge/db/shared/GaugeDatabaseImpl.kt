package com.gauge.db.shared

import com.gauge.db.GaugeDatabase
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import comgaugedb.Events
import comgaugedb.MydbQueries
import comgaugedb.SessionInfo
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

internal val KClass<GaugeDatabase>.schema: SqlDriver.Schema
  get() = GaugeDatabaseImpl.Schema

internal fun KClass<GaugeDatabase>.newInstance(driver: SqlDriver): GaugeDatabase =
    GaugeDatabaseImpl(driver)

private class GaugeDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), GaugeDatabase {
  override val mydbQueries: MydbQueriesImpl = MydbQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE IF NOT EXISTS SessionInfo (
          |trackedAt INTEGER NOT NULL,
          |Id INTEGER NOT NULL PRIMARY KEY
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE IF NOT EXISTS Events (
          |eventName TEXT NOT NULL,
          |eventType TEXT NOT NULL,
          |attributes TEXT,
          |trackedAt INTEGER NOT NULL,
          |sessionId INTEGER NOT NULL,
          |FOREIGN KEY(sessionId) REFERENCES SessionInfo(Id)
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class MydbQueriesImpl(
  private val database: GaugeDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), MydbQueries {
  internal val selectSession: MutableList<Query<*>> = copyOnWriteList()

  internal val filterEventOnSessionId: MutableList<Query<*>> = copyOnWriteList()

  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectSession(mapper: (trackedAt: Long, Id: Long) -> T): Query<T> =
      Query(109109822, selectSession, driver, "mydb.sq", "selectSession",
      "SELECT * FROM SessionInfo") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  override fun selectSession(): Query<SessionInfo> = selectSession { trackedAt, Id ->
    SessionInfo(
      trackedAt,
      Id
    )
  }

  override fun <T : Any> filterEventOnSessionId(sessionId: Long, mapper: (
    eventName: String,
    eventType: String,
    attributes: String?,
    trackedAt: Long,
    sessionId: Long
  ) -> T): Query<T> = FilterEventOnSessionIdQuery(sessionId) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getLong(3)!!,
      cursor.getLong(4)!!
    )
  }

  override fun filterEventOnSessionId(sessionId: Long): Query<Events> =
      filterEventOnSessionId(sessionId) { eventName, eventType, attributes, trackedAt, sessionId_ ->
    Events(
      eventName,
      eventType,
      attributes,
      trackedAt,
      sessionId_
    )
  }

  override fun <T : Any> selectAll(mapper: (
    eventName: String,
    eventType: String,
    attributes: String?,
    trackedAt: Long,
    sessionId: Long
  ) -> T): Query<T> = Query(-1285296823, selectAll, driver, "mydb.sq", "selectAll",
      "SELECT * FROM Events") { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getLong(3)!!,
      cursor.getLong(4)!!
    )
  }

  override fun selectAll(): Query<Events> = selectAll { eventName, eventType, attributes, trackedAt,
      sessionId ->
    Events(
      eventName,
      eventType,
      attributes,
      trackedAt,
      sessionId
    )
  }

  override fun insertSession(trackedAt: Long, Id: Long?) {
    driver.execute(-1125853727, """INSERT OR REPLACE INTO SessionInfo(trackedAt,Id)VALUES(?,?)""",
        2) {
      bindLong(1, trackedAt)
      bindLong(2, Id)
    }
    notifyQueries(-1125853727, {database.mydbQueries.selectSession})
  }

  override fun insertEvent(
    eventName: String,
    eventType: String,
    attributes: String?,
    trackedAt: Long,
    sessionId: Long
  ) {
    driver.execute(1465719973,
        """INSERT OR REPLACE INTO Events(eventName, eventType, attributes, trackedAt, sessionId)VALUES(?,?,?,?,?)""",
        5) {
      bindString(1, eventName)
      bindString(2, eventType)
      bindString(3, attributes)
      bindLong(4, trackedAt)
      bindLong(5, sessionId)
    }
    notifyQueries(1465719973, {database.mydbQueries.filterEventOnSessionId +
        database.mydbQueries.selectAll})
  }

  override fun deleteAll() {
    driver.execute(71969338, """DELETE FROM Events""", 0)
    notifyQueries(71969338, {database.mydbQueries.filterEventOnSessionId +
        database.mydbQueries.selectAll})
  }

  private inner class FilterEventOnSessionIdQuery<out T : Any>(
    @JvmField
    val sessionId: Long,
    mapper: (SqlCursor) -> T
  ) : Query<T>(filterEventOnSessionId, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(-1909733364, """
    |SELECT *
    |FROM Events
    |WHERE sessionId =?
    """.trimMargin(), 1) {
      bindLong(1, sessionId)
    }

    override fun toString(): String = "mydb.sq:filterEventOnSessionId"
  }
}
