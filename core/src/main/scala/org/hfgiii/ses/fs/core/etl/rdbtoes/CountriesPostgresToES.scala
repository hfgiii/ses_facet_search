package org.hfgiii.ses.fs.core.etl.rdbtoes

import org.hfgiii.ses.common.SesCommon._
import org.hfgiii.ses.fs.core.etl.PostgresDB
import com.sksamuel.elastic4s.ElasticDsl._

import fstables.Tables._
import fstables.Tables.profile.simple._


object CountriesPostgresToES extends PostgresDB with ES {

  def dropNull(kv:(String,Any)*) =
    kv.foldLeft(List.empty[(String,Any)]){
      (l,kv) =>

      val (k,v) = kv

      v match {
        case Some(v) => (k -> v) :: l
        case None    => l
        case v       => (k -> v) :: l
      }
    }

  def main(args:Array[String]) =

        withES {

          val countriesIndexed =

          withDB {  implicit session =>

            Countries.run.map { c =>
              index into "countries" fields dropNull("commonName" -> c.commonName,
                "formalName" -> c.formalName,
                "_Type" -> c._Type,
                "subType" -> c.subType,
                "sovereignty" -> c.sovereignty,
                "capital" -> c.capital,
                "iso4217CurrencyCode" -> c.iso4217CurrencyCode,
                "iso4217CurrencyName" -> c.iso4217CurrencyName,
                "ituTTelephoneCode" -> c.ituTTelephoneCode,
                "iso316612LetterCode" -> c.iso316612LetterCode,
                "iso316613LetterCode" -> c.iso316613LetterCode,
                "iso31661Number" -> c.iso31661Number,
                "ianaCountryCodeTld" -> c.ianaCountryCodeTld

              )
            }

        }

        bulkIndexLoad("countries", countriesIndexed, countriesIndexed.length)

    }

}
