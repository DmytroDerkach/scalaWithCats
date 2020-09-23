package tutorial.chapter1.typeclasses.showexample

import java.util.Date

import cats.Show
import cats.implicits.toShow
import cats.instances.int._
import cats.instances.string._



object ShowExample extends App {

  val showInt: Show[Int] = Show.apply[Int]
  val showString: Show[String] = Show.apply[String]

  val intAsString: String = showInt.show(123) // intAsString: String = "123"
  val stringAsString: String = showString.show("abc") // stringAsString: String = "abc"

  val shownInt = 123.show // shownInt: String = "123"
  val shownString = "abc".show // shownString: String = "abc"

  println(s"$intAsString and $stringAsString and $shownInt and $shownString")

  // Defining Custom Instances

  implicit val dateShow: Show[Date] = {
    new Show[Date] {
      def show(date: Date): String = s"${date.getTime}ms since the epoch."
    }
  }
  /*
    implicit val dateShow: Show[Date] =
    Show.show(date => s"${date.getTime}ms since the epoch.")
  */

  println((new Date()).show)

}
