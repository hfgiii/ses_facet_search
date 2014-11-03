
import sbt.Keys._
import sbt._

import scala.util.Properties

object SesFacetSearchBuild extends Build {
  import Deps._
  import Reps._
  import SlickConfig._

  val buildOrganization = "org.hfgiii.ses.fs"
  val buildVersion = "0.1.0-SNAPSHOT"
  val buildPublishTo = None
  val buildScalaVersion = "2.11.2"

  val buildParentName = "parent"

  val BaseSettings = Project.defaultSettings ++ Seq(
    organization := buildOrganization,
    publishTo := buildPublishTo,
    scalaVersion := buildScalaVersion,
    crossScalaVersions := Seq("2.10.2", "2.11.2"),
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature"),
    resolvers := reps)

  def sesFacetSearchProject(projectName: String): Project = {
    Project(
      id = projectName,
      base = file(projectName),
      settings = BaseSettings ++ Seq(
        name := projectName,
        version := buildVersion))
  }

  lazy val root = Project(
    id = buildParentName,
    base = file("."),
    settings = BaseSettings) aggregate(codegenProject,fsTablesProject,facetsearchProject,corefsProject)


  lazy val codegenProject =
      sesFacetSearchProject("codegen").
    settings (libraryDependencies ++= slickDeps ++ logDeps ++ slickCodeGenDeps)


  lazy val corefsProject =
    sesFacetSearchProject("core").
      settings(libraryDependencies := deps ++ testingDeps).
      dependsOn (fsTablesProject)


  lazy val fsTablesProject =
    sesFacetSearchProject("fstables").
  settings(
    libraryDependencies ++= deps ++ slickDeps ++ slickCodeGenDeps ++ testingDeps,
    slick <<= slickCodeGenTask,
    sourceGenerators in Compile <+= slickCodeGenTask).
    dependsOn (codegenProject)


  lazy val facetsearchProject =
    sesFacetSearchProject("facetsearch").dependsOn(corefsProject).
      settings(libraryDependencies := deps ++  testingDeps)


}

object Reps {
  val reps = Seq(
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/releases/",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "gphat" at "https://raw.github.com/gphat/mvn-repo/master/releases/")
}

object Deps {

  def withExclusions(deps: Seq[ModuleID]): Seq[ModuleID] = {
    deps.map(_.exclude("commons-logging", "commons-logging"))
  }

  val slickCodeGenDeps = withExclusions(List(
    "com.typesafe.slick" %% "slick-codegen" % "2.1.0"))

  val slickDeps = withExclusions(List(
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
    "com.h2database" % "h2" % "1.4.181" % "test"))

  val logDeps = withExclusions(List(
    "org.slf4j" % "jcl-over-slf4j" % "1.7.7" intransitive,
    "org.slf4j" % "slf4j-api" % "1.7.7" intransitive,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.0.0",
    "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
    "ch.qos.logback" % "logback-core" % "1.1.2" % "runtime"))

  val deps = withExclusions(List(
    "org.hfgiii" %% "ses_common_macros" % "0.1.0-SNAPSHOT",
    "org.hfgiii" %% "ses_common" % "0.1.0-SNAPSHOT",
    "org.parboiled"    %% "parboiled" % "2.0.1",
    "com.novus"         %% "salat-core" % "1.9.9",
    "com.novus"         %% "salat-util" % "1.9.9",
    "com.chuusai" %% "shapeless" % "2.0.0",
    "com.sksamuel.elastic4s" % "elastic4s_2.11" %  "1.4.0.Beta1", 
    "org.elasticsearch" % "elasticsearch" % "1.4.0.Beta1",
    "com.github.scopt" %% "scopt" % "3.2.0",
    "org.scala-lang.modules" %% "scala-async" % "0.9.2",
    "org.scalatra.scalate" % "scalate-core_2.11" % "1.7.0",
    "org.codehaus.groovy"  %  "groovy" % "2.3.7"))

  val testingDeps = withExclusions(List(
    "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
    "org.specs2" %% "specs2" % "2.4.2" % "test",
    "org.elasticsearch" % "elasticsearch" % "1.3.2" % "test",
    "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test"))
}

object SlickConfig {
  val FS_SQL_URL = Properties.envOrElse("FS_SQL_URL", "jdbc:postgresql://localhost:5432/essource")
  val FS_SQL_USER = Properties.envOrElse("FS_SQL_USER", "rush")
  val FS_SQL_PASS = Properties.envOrElse("FS_SQL_PASS", "manga927")

  val slick = TaskKey[Seq[File]]("gen-tables")
  val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "slick-generated").getPath // place generated files in sbt's managed sources folder
  val jdbcDriver = "org.postgresql.Driver"
    val slickDriver = "scala.slick.driver.PostgresDriver"
    val pkg = "fstables"
    toError(r.run("org.hfgiii.ses.fs.codgen.CustomCodeGenerator", cp.files,
      Array(slickDriver, jdbcDriver, FS_SQL_URL, outputDir, pkg, FS_SQL_USER, FS_SQL_PASS), s.log))
    val fname = outputDir + "/fstables/Tables.scala"
    Seq(file(fname))
  }
}
