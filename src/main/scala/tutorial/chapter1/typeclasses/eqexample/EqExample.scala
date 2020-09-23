package tutorial.chapter1.typeclasses.eqexample

import java.util.Date

import cats.Eq
import cats.instances.int._
import cats.syntax.eq._
import cats.instances.option._
import cats.syntax.option._
import cats.instances.long._

object EqExample extends App {
  val eqInt = Eq[Int]

  private val bool1: Boolean = eqInt.eqv(123, 123)
  private val bool2: Boolean = eqInt.eqv(123, 124)

  println(s"$bool1 & $bool2")

  123 === 123 // res4: Boolean = true
  123 =!= 234 // res5: Boolean = true

  /*
   comparing values of different types causes a compiler error:
    123 === "123"
    // error: type mismatch;
    //  found   : String("123")
    //  required: Int
    // 123 === "123"
    //         ^^^^^
   */

  //Comparing Options -----------------------------------------------
  (Some(1) : Option[Int]) === (None : Option[Int]) // res8: Boolean = false
  Option(1) === Option.empty[Int]                  // res9: Boolean = false

  1.some === none[Int] // res10: Boolean = false
  1.some =!= none[Int] // res11: Boolean = true

  //Comparing Custom Types -----------------------------------------------
  implicit val dateEq: Eq[Date] = Eq.instance[Date] {
    (date1, date2) => date1.getTime === date2.getTime
  }

  val x = new Date() // now
  val y = new Date() // a bit later than now
  x === x // res12: Boolean = true
  x === y // res13: Boolean = false
}
