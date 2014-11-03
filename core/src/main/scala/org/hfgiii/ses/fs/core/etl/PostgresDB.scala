package org.hfgiii.ses.fs.core.etl


import com.typesafe.config.ConfigFactory
import fstables.Tables.profile.simple._

import scala.util.Properties

trait PostgresDB {

  protected val config = ConfigFactory.load(Properties.envOrElse("SF_ENV","application"))

  protected val db = Database.forURL(url = config.getString("fs.core.db.url"),
                              driver     = config.getString("fs.core.db.driver"),
                              user       = config.getString("fs.core.db.user"),
                              password   = config.getString("fs.core.db.password"))

  def withDB[T](ex: Session => T):T =
    db.withSession (ex)
}
