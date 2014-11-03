import sbt._

resolvers ++= Seq(
  Classpaths.typesafeReleases
)

addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.10.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
