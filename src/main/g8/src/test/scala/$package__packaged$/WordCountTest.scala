// Copyright (C) 2018
package $organization$.$name$

/**
 * A simple test for everyone's favourite wordcount example.
 */

import com.holdenkarau.spark.testing.{ DatasetSuiteBase, SharedSparkContext }
import org.scalatest.{ FlatSpec, Matchers }

class WordCountTest extends FlatSpec with Matchers with SharedSparkContext with DatasetSuiteBase {
  "A word count service" should "remove stop words and split on delimiter tokens and count words" in {
    val linesRDD = sc.parallelize(Seq(
      "How happy was the panda? You ask.",
      "Panda is the most happy panda in all the#!?ing land!"))

    val stopWords: Set[String] = Set("a", "the", "in", "was", "there", "she", "he")
    val splitTokens: Array[Char] = "#%?!. ".toCharArray

    val wordCounts = WordCount.withStopWordsFiltered(
      linesRDD, splitTokens, stopWords)
    val wordCountsAsMap = wordCounts.collectAsMap()
    assert(!wordCountsAsMap.contains("the"))
    assert(!wordCountsAsMap.contains("?"))
    assert(!wordCountsAsMap.contains("#!?ing"))
    assert(wordCountsAsMap.contains("ing"))
    assert(wordCountsAsMap.get("panda").get.equals(3))
  }
}
