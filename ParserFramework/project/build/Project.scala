import sbt._

class ParserExperiment(info:ProjectInfo) extends DefaultProject(info) {
	 val junitInterface = "com.novocode" % "junit-interface" % "0.5" % "test->default"
}