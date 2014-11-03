package org.hfgiii.ses.fs.codgen

import scala.slick.codegen.SourceCodeGenerator
import scala.slick.jdbc.meta.MTable
import scala.slick.model.Model

/**
 * Created by ctcarrier on 7/21/14.
 */

class CustomCodeGenerator(model: Model) extends SourceCodeGenerator(model) {
  override def Table = new Table(_) {
    override def autoIncLastAsOption = true
  }
}

object CustomCodeGenerator {

  import scala.reflect.runtime.currentMirror
  import scala.slick.driver.JdbcProfile

  def main(args: Array[String]) = {
    args.toList match {
      case slickDriver :: jdbcDriver :: url :: outputFolder :: pkg :: tail if tail.size == 0 || tail.size == 2 =>
        val driver: JdbcProfile = {
          val module = currentMirror.staticModule(slickDriver)
          val reflectedModule = currentMirror.reflectModule(module)
          val driver = reflectedModule.instance.asInstanceOf[JdbcProfile]
          driver
        }
        val db = driver.simple.Database
        (tail match {
          case user :: password :: Nil => db.forURL(url, driver = jdbcDriver, user = user, password = password)
          case Nil => db.forURL(url, driver = jdbcDriver)
          case _ => throw new Exception("This should never happen.")
        }).withSession { implicit session =>
          val ourTables: Option[Seq[MTable]] = Some(MTable.getTables(None, None, None, Some(Seq("TABLE", "VIEW"))).list)
          new CustomCodeGenerator(driver.createModel(ourTables)).writeToFile(slickDriver, outputFolder, pkg)
        }
    }
  }
}

