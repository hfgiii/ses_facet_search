package org.hfgiii.ses.fs.core.etl.csvtordb

import org.hfgiii.ses.fs.core.etl.PostgresDB
import org.parboiled2.ParserInput
import org.hfgiii.ses.fs.core.csv.CsvParsers._
import fstables.Tables._
import fstables.Tables.profile.simple._

object TrafficAccidentsCsvToPostgres extends PostgresDB{

  def main(args:Array[String]) {
    val finput = this.getClass.getResourceAsStream("/traffic_accidents.csv")

    val inputfile: ParserInput = io.Source.fromInputStream(finput).mkString

     val trafficAccidents =
     TrafficAccidentsParser(inputfile).parseTrafficAccidents.toSeq

    db.withSession { implicit session =>
      TrafficAccidents ++= trafficAccidents
    }
  }
}
