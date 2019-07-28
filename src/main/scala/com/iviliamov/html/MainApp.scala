package com.iviliamov.html

import java.io.File

import com.typesafe.config.ConfigFactory
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.util.{Failure, Success, Try}

object MainApp extends App {
  private val CHARSET_NAME = "utf8"

  if (args.length < 2) {
    println("args: <input_origin_file_path> <input_other_sample_file_path>")
  }

  val config = ConfigFactory.load()

  val originElementId = config.getString("origin-element-id")
  val originFile: File = new File(args(0))
  val targetFile = new File(args(1))

  val result = SimilarElementFinder.find(originElementId, loadFile(originFile), loadFile(targetFile))

  println("Result selector is:")
  println("  " + result)

  def loadFile(htmlFile: File): Element = Try {
    Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath)
  } match {
    case Success(file) => file
    case Failure(ex) =>
      println("Error occurred. " + ex)
      throw ex
  }

}