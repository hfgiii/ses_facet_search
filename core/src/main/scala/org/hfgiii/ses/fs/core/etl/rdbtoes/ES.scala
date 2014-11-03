package org.hfgiii.ses.fs.core.etl.rdbtoes

import java.io.File
import java.util.UUID

import org.elasticsearch.common.settings.ImmutableSettings
import org.hfgiii.ses.common.SesCommon._

trait ES {

  val tempFile = File.createTempFile("elasticsearchtests", "tmp")
  val homeDir = new File(tempFile.getParent + "/" + UUID.randomUUID().toString)
  homeDir.mkdir()
  homeDir.deleteOnExit()
  tempFile.deleteOnExit()

  val settings = ImmutableSettings.settingsBuilder()
    .put("node.http.enabled", false)
    .put("http.enabled", false)
    .put("path.home", homeDir.getAbsolutePath)
    .put("index.number_of_shards", 1)
    .put("index.number_of_replicas", 0)
    .put("script.disable_dynamic", false)
    .put("es.logger.level", "INFO")

  implicit val client = initLocalEs4sClient(settings)

}
