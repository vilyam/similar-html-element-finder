lazy val root = (project in file(".")).settings(
  scalaVersion := "2.11.8",

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.jcenterRepo
  ),

  libraryDependencies ++= Seq(
    "org.jsoup" % "jsoup" % "1.11.2", 
    "com.typesafe" % "config" % "1.3.4"),

  mainClass in(Compile, packageBin) := Some("com.iviliamov.html.MainApp")
)