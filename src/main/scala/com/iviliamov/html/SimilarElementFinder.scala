package com.iviliamov.html

import org.jsoup.nodes.Element

import scala.collection.JavaConversions._

case class ElementMeta(attributes: Map[String, String], path: String)

case class ElementEstimation(element: ElementMeta, estimation: Int)

object SimilarElementFinder {

  def find(originId: String, originFile: Element, targetFile: Element): String = {
    val element = originFile.getElementById(originId)

    val originElementAttributes = aggregateAttributes(element)

    val targetElements = element.classNames()
      .flatMap(className =>
        targetFile.getElementsByClass(className)
          .map(el => ElementMeta(aggregateAttributes(el), selector(el)))
      )

    val candidates = targetElements
      .map(el => ElementEstimation(el, estimator(originElementAttributes, el.attributes)))
      .filter(_.estimation > 0)
      .toList.sortBy(-_.estimation)

    println("The most relevant elements are [Estimation, Selector]:")
    candidates.foreach(el => println("  [" + el.estimation + ", " + el.element.path + "]"))

    candidates.maxBy(_.estimation).element.path
  }

  def selector(el: Element): String = el.cssSelector()

  def aggregateAttributes(el: Element): Map[String, String] = el
    .attributes().asList()
    .map(el => el.getKey -> el.getValue)
    .toMap

  def estimator(origin: Map[String, String], target: Map[String, String]): Int = {
    var estimator = 0

    origin.foreach { entry =>
      val key = entry._1

      if (target.contains(key)) estimator += 1
      target.get(key).foreach(v => if (v.equals(entry._2)) estimator += 1)
    }

    target.foreach { entry =>
      val key = entry._1

      if (!origin.contains(key)) estimator -= 1
      origin.get(key).foreach(v => if (!v.equals(entry._2)) estimator -= 1)
    }

    estimator
  }
}
