import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "clavin-server"
  val appVersion      = "1.0.0-RELEASE"
 
  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "com.bericotech" % "clavin" % "2.0.0"
    //"com.google.code.gson" % "gson" % "2.2.4"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here     
      
    //resolvers += "Berico repository" at "http://nexus.bericotechnologies.com/content/groups/public/",
    resolvers += "OpenNLP repository" at "http://opennlp.sourceforge.net/maven2/"
  )

}
