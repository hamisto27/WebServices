import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "seriespotAPI"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.29",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.1",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.3.0",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.3.1",
    "javax.xml.bind" % "jaxb-api" % "2.2",
    "commons-codec" % "commons-codec" % "1.9",
    "org.apache.commons" % "commons-lang3" % "3.3.1",
    "org.eclipse.persistence" % "eclipselink" % "2.5.1",
    "net.sf.jtidy" % "jtidy" % "r938",
    "org.antlr" % "stringtemplate" % "4.0.2"
            
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}