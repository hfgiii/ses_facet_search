package org.hfgiii.ses.fs.core.etl.csvtordb

import org.hfgiii.ses.fs.core.etl.PostgresDB
import org.parboiled2.ParserInput
import org.hfgiii.ses.fs.core.csv.CsvParsers._
import fstables.Tables._
import fstables.Tables.profile.simple._


object CountriesCsvToPostgres extends PostgresDB {

  def main(args:Array[String]) {
    val finput = this.getClass.getResourceAsStream("/iso_3166_2_countries.csv")

    val inputfile: ParserInput = io.Source.fromInputStream(finput).mkString

    val countries = CountriesParser(inputfile).parseCountries.toSeq

    db.withSession { implicit session =>
       Countries ++= countries
    }
  }
}
