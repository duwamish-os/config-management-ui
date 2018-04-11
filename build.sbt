name := "config-management-ui"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.1.0"

libraryDependencies += "org.iq80.leveldb" % "leveldb-api" % "0.10"

libraryDependencies += "org.iq80.leveldb" % "leveldb" % "0.10"

libraryDependencies += "org.scalatra" %% "scalatra" % "2.6.3"
libraryDependencies += "org.scalatra" %% "scalatra-json" % "2.6.3"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.2"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"

enablePlugins(JettyPlugin)
