lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "LNKD",
  scalaVersion := "2.11.7"
)

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.9.2",
  "com.twitter" %% "scrooge-core" % "3.20.0",
  "com.twitter" %% "finagle-thrift" % "6.29.0",
  "com.googlecode.grep4j" % "grep4j" % "1.8.7"
)

lazy val app = project.in(file("app"))
  .settings(commonSettings: _*)
  .settings(
    scroogeThriftSourceFolder in Compile <<= baseDirectory {
      base => base / "src/main/thrift"
    }
  )