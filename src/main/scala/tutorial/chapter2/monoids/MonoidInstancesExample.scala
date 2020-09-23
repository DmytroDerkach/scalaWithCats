package tutorial.chapter2.monoids

import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.instances.int._ // for Monoid
import cats.instances.option._ // for Monoid
import cats.instances.map._ // for Monoid

import cats.Semigroup
import cats.syntax.semigroup._ // for |+|


object MonoidInstancesExample extends App {
  Monoid[String].combine("Hi ", "there") // res0: String = "Hi there"
  Monoid[String].empty // res1: String = ""

  Monoid.apply[String].combine("Hi ", "there") // res2: String = "Hi there"
  Monoid.apply[String].empty // res3: String = ""

  Semigroup[String].combine("Hi ", "there") // res4: String = "Hi there"

  private val i: Int = Monoid[Int].combine(1, 5)
  println(i)

  val a = Option(22) // a: Option[Int] = Some(22)
  val b = Option(20) // b: Option[Int] = Some(20)
  private val maybeInt: Option[Int] = Monoid[Option[Int]].combine(a, b) // res6: Option[Int] = Some(42)
  println(maybeInt)

  // Monoid Syntax -------------------------------------------
  val stringResult = "Hi " |+| "there" |+| Monoid[String].empty // stringResult: String = "Hi there"
  println(stringResult)

  val intResult = 1 |+| 2 |+| Monoid[Int].empty // intResult: Int = 3

  private val items = List(1, 2, 3)
  private val sumItems: Int = items.foldLeft(Monoid[Int].empty)(_ |+| _)
  println(sumItems)

  val map1 = Map("a" -> 1, "b" -> 2)
  val map2 = Map("b" -> 3, "d" -> 4)
  map1 |+| map2 // res2: Map[String, Int] = Map("b" -> 5, "d" -> 4, "a" -> 1)

}
