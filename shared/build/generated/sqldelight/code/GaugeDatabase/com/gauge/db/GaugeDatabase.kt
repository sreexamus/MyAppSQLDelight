package com.gauge.db

import com.gauge.db.shared.newInstance
import com.gauge.db.shared.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import comgaugedb.MydbQueries

interface GaugeDatabase : Transacter {
  val mydbQueries: MydbQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = GaugeDatabase::class.schema

    operator fun invoke(driver: SqlDriver): GaugeDatabase =
        GaugeDatabase::class.newInstance(driver)}
}
