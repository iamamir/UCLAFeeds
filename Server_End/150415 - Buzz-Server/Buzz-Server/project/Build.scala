import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "mPOSMiddleware"
  val appVersion      = "1.0"

  val appDependencies = Seq(
      javaCore,
      javaJdbc,
      javaJpa,
      "commons-io" % "commons-io" % "2.3",
      "mysql" % "mysql-connector-java" % "5.1.18",
      "org.hibernate" % "hibernate-entitymanager" % "4.2.2.Final"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // disable Ebean ORM
    ebeanEnabled := false
  )

}
