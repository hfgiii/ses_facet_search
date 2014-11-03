package org.hfgiii.ses.fs.core.csv

import java.sql.{Timestamp, Date}

import org.hfgiii.ses.common.SesCommon._
import fstables.Tables._
import org.parboiled2.{ParseError, ParserInput}

import scala.util.{Failure, Success}
import shapeless._
import syntax.typeable._

trait CsvParsers {

  private [csv] trait CSVParboiledParserCountries  extends CSVParboiledParserSB[CountriesRow] {
    def name = "countries"

    def optCol(str:String):Option[String] =
       if(str.isEmpty) None
       else Some(str)

    def genOutput =
      (r: Seq[String]) => {
        println(r(0))

        CountriesRow(
          commonName = r(1),
          formalName = optCol(r(2)),
          _Type = r(3),
          subType = optCol(r(4)),
          sovereignty = optCol(r(5)),
          capital = optCol(r(6)),
          iso4217CurrencyCode = optCol(r(7)),
          iso4217CurrencyName = optCol(r(8)),
          ituTTelephoneCode = optCol(r(9)),
          iso316612LetterCode = optCol(r(10)),
          iso316613LetterCode = optCol(r(11)),
          iso31661Number = optCol(r(12)).map(_.toInt),
          ianaCountryCodeTld = optCol(r(13))
        )
      }

  }


  case class CountriesParser(input: ParserInput) extends CSVParboiledParserCountries with CSVParserIETFAction {
    def parseCountries:List[CountriesRow] =
      csvfile.run() match {
        case Success(result) => result.cast[List[CountriesRow]].fold(List.empty[CountriesRow])(countries => countries)

        case Failure(e: ParseError) => println("Expression is not valid: " + formatError(e)) ; List.empty[CountriesRow]
        case Failure(e) => println("Unexpected error during parsing run: " + e) ; List.empty[CountriesRow]
      }
  }


 private [csv] trait CSVParboiledParserTrafficAccidents  extends CSVParboiledParserSB[TrafficAccidentsRow] {
   def name = "trafficAccidents"
   def genOutput =
     (r: Seq[String]) =>
       TrafficAccidentsRow (
           incidentId = r(0).toFloat.toLong,
           offenseId =  r(1).toLong,
           offenseCode = r(2).toInt,
           offenseCodeExtension = r(3).toInt,
           offenseTypeId = r(4),
           offenseCategoryId = r(5),
           firstOccurrenceDate = Timestamp.valueOf(r(6)),
           lastOccurrenceDate = None,
           reportedDate = Timestamp.valueOf(r(8)),
           incidentAddress = r(9),
           geoX = r(10).toFloat,
           geoY = r(11).toFloat,
           geoLon = r(12).toFloat,
           geoLat = r(13).toFloat,
           districtId = r(14).toInt,
           precinctId = r(15).toInt,
           neighborhoodId = r(16)
       )

 }

  case class TrafficAccidentsParser(input: ParserInput) extends CSVParboiledParserTrafficAccidents with CSVParserIETFAction {
    def parseTrafficAccidents:List[TrafficAccidentsRow] =
      csvfile.run() match {
        case Success(result) => result.cast[List[TrafficAccidentsRow]].fold(List.empty[TrafficAccidentsRow])(accidents => accidents)

        case Failure(e: ParseError) => println("Expression is not valid: " + formatError(e)) ; List.empty[TrafficAccidentsRow]
        case Failure(e) => println("Unexpected error during parsing run: " + e) ; List.empty[TrafficAccidentsRow]
      }
  }
}

object CsvParsers extends CsvParsers
