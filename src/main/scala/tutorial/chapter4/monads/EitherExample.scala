package tutorial.chapter4.monads
import cats.syntax.either._ // for asRight
object EitherExample extends App {
  val either1: Either[String, Int] = Right(10)
  val either2: Either[String, Int] = Right(32)

  val y2 = for {
    a <- either1
    b <- either2
  } yield (a + b)

  println(y2)

  val a = 3.asRight[String] // a: Either[String, Int] = Right(3)
  val b = 4.asRight[String] // b: Either[String, Int] = Right(4)
  for {
    x <- a
    y <- b
  } yield x*x + y*y // res3: Either[String, Int] = Right(25)


  def countPositive(nums: List[Int]) = nums.foldLeft(0.asRight[String]) {
    (accumulator, num) =>
      if(num > 0) {
        accumulator.map(_ + 1)
      } else {
        Left("Negative. Stopping!")
      }
  }

  countPositive(List(1, 2, 3)) // res5: Either[String, Int] = Right(3)
  countPositive(List(1, -2, 3)) // res6: Either[String, Int] = Left("Negative. Stopping!")

  //---------
  // The catchOnly and catchNonFatal methods are great for capturing Exceptions as instances of Either:
  Either.catchOnly[NumberFormatException]("foo".toInt)
  // res7: Either[NumberFormatException, Int] = Left(
  // java.lang.NumberFormatException: For input string: "foo"
  // )
  Either.catchNonFatal(sys.error("Badness"))
  // res8: Either[Throwable, Nothing] = Left(java.lang.RuntimeException: Badness)


  // ----------- Transforming Eithers
  "Error".asLeft[Int].getOrElse(0) // res11: Int = 0
  "Error".asLeft[Int].orElse(2.asRight[String]) // res12: Either[String, Int] = Right(2)

  "error".asLeft[Int].recover { case _: String => -1 } // res14: Either[String, Int] = Right(-1)
  "error".asLeft[Int].recoverWith { case _: String => Right(-1)} // res15: Either[String, Int] = Right(-1)

  "foo".asLeft[Int].leftMap(_.reverse) // res16: Either[String, Int] = Left("oof")
  6.asRight[String].bimap(_.reverse, _ * 7) // res17: Either[String, Int] = Right(42)
  "bar".asLeft[Int].bimap(_.reverse, _ * 7) // res18: Either[String, Int] = Left("rab")

  123.asRight[String] // res19: Either[String, Int] = Right(123)
  123.asRight[String].swap // res20: Either[Int, String] = Left(123)
}
