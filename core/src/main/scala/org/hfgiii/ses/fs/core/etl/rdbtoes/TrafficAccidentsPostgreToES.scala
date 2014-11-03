package org.hfgiii.ses.fs.core.etl.rdbtoes

import org.hfgiii.ses.common.SesCommon._
import org.hfgiii.ses.fs.core.etl.PostgresDB
import com.sksamuel.elastic4s.ElasticDsl._

import fstables.Tables._
import fstables.Tables.profile.simple._

object TrafficAccidentsPostgreToES extends PostgresDB with ES {

  def main(args:Array[String]) {

        withES {

          val accientsIndexed =

            withDB { implicit session =>

              TrafficAccidents.run.map { ta =>

                index into "traffic_accidents" fields("incidentId" -> ta.incidentId,
                  "offenseId" -> ta.offenseId,
                  "offenseCode" -> ta.offenseCode,
                  "offenseCodeExtension" -> ta.offenseCodeExtension,
                  "offenseTypeId" -> ta.offenseTypeId,
                  "offenseCategoryId" -> ta.offenseCategoryId,
                  "firstOccurrenceDate" -> ta.firstOccurrenceDate,
                  // "lastOccurrenceDate" -> ta.lastOccurrenceDate, //Option
                  "reportedDate" -> ta.reportedDate,
                  "incidentAddress" -> ta.incidentAddress,
                  "geoX" -> ta.geoX,
                  "geoY" -> ta.geoY,
                  "geoLon" -> ta.geoLon,
                  "geoLat" -> ta.geoLat,
                  "districtId" -> ta.districtId,
                  "precinctId" -> ta.precinctId,
                  "neighborhoodId" -> ta.neighborhoodId
                )

              }.toSeq
            }

            bulkIndexLoad("traffic_accidents", accientsIndexed, accientsIndexed.length)
        }
   }
}
