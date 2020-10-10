package tutorial.chapter6.semigroupal_and_applicative

import cats.implicits.catsKernelStdMonoidForVector
import cats.{Monoid, Semigroupal}
import cats.instances.option._
import cats.instances.int._
import cats.instances.invariant._
import cats.instances.list._
import cats.instances.string._
import cats.syntax.apply._
import cats.syntax.semigroup._
import cats.instances.future._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.either._
import cats.syntax.parallel._

import scala.concurrent.Future // for |+|

final case class Cat(name: String, born: Int, color: String)
object SemigroupalAndApplicativeExample extends App {
  /*
    Semigroupal allows us to join contexts.
   */
  private val maybeTuple: Option[(Int, String)] = Semigroupal[Option].product(Some(123), Some("abc")) // res1: Option[(Int, String)] = Some((123, "abc"))

  println(maybeTuple)

  private val maybeTuple1: Option[(Nothing, String)] = Semigroupal[Option].product(None, Some("abc")) // res2: Option[Tuple2[Nothing, String]] = None
  println(maybeTuple1)
  private val maybeTuple2: Option[(Int, Nothing)] = Semigroupal[Option].product(Some(123), None) // res3: Option[Tuple2[Int, Nothing]] = None
  println(maybeTuple2)

  //----- Joining Three or More Contexts -----
  private val maybeTuple3: Option[(Int, Int, Int)] = Semigroupal.tuple3(Option(1), Option(2), Option(3)) // res4: Option[(Int, Int, Int)] = Some((1, 2, 3))

  val a = Option(1)
  val b = Option(2)
  val c = Option.empty[Int]
  private val maybeTuple4: Option[(Int, Int, Int)] = Semigroupal.tuple3(a, b, c) // res5: Option[(Int, Int, Int)] = None
  println(s"maybeTuple4 = $maybeTuple4")

  Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _) // res6: Option[Int] = Some(6)

  Semigroupal.map2(Option(1), Option.empty[Int])(_ + _) // res7: Option[Int] = None

  // -------- Apply Syntax -----
  private val tupled: Option[(Int, String)] = (Option(123), Option("abc")).tupled
  println(tupled)
  private val tupled1: Option[(Int, String, Boolean)] = (Option(123), Option("abc"), Option(true)).tupled

 (Option("Garfield"),
  Option(1978),
  Option("Orange & black")).mapN(Cat.apply)

  // ---- Fancy Functors and Apply Syntax ----
  val tupleToCat: (String, Int, String) => Cat = Cat.apply
  val catToTuple: Cat => (String, Int, String) = cat => (cat.name, cat.born, cat.color)
  implicit val catMonoid: Monoid[Cat] = (
    Monoid[String],
    Monoid[Int],
    Monoid[String]
    ).imapN(tupleToCat)(catToTuple)


  val garfield = Cat("Garfield", 1978, "Lasagne")
  val heathcliff = Cat("Heathcliff", 1988, "Junk Food")

  val pls = garfield |+| heathcliff // res14: Cat = Cat("GarfieldHeathcliff", 3966, "LasagneJunk Food")
  println(pls)

  val futureCat = (
    Future("Garfield"),
    Future(1978),
    Future("Lasagne")
    ).mapN(Cat.apply)

  Await.result(futureCat, 1.second) // res1: Cat = Cat("Garfield", 1978, "Lasagne")

  // ----- LISTS -----
  Semigroupal[List].product(List(1, 2), List(3, 4)) // res2: List[(Int, Int)] = List((1, 3), (1, 4), (2, 3), (2, 4))
  // ----- Either -----
  type ErrorOr[A] = Either[Vector[String], A]
  private val value: ErrorOr[(Nothing, Nothing)] = Semigroupal[ErrorOr].product(Left(Vector("Error 1")), Left(Vector("Error 2"))) // res3: ErrorOr[Tuple2[Nothing, Nothing]] = Left(Vector("Error 1"))

  val error1: ErrorOr[Int] = Left(Vector("Error 1"))
  val error2: ErrorOr[Int] = Left(Vector("Error 2"))
  Semigroupal[ErrorOr].product(error1, error2) // res0: ErrorOr[(Int, Int)] = Left(Vector("Error 1"))
        (error1, error2).tupled //                res1: ErrorOr[(Int, Int)] = Left(Vector("Error 1"))
  /*
  To collect all the errors we simply replace tupled with its “parallel” version called parTupled.
   */
  private val tupled2: ErrorOr[(Int, Int)] = (error1, error2).parTupled // res2: ErrorOr[(Int, Int)] = Left(Vector("Error 1", "Error 2"))
  println(tupled2)

  val success1: ErrorOr[Int] = Right(1)
  val success2: ErrorOr[Int] = Right(2)
  val addTwo = (x: Int, y: Int) => x + y
  (error1, error2).parMapN(addTwo) //     res4: ErrorOr[Int] = Left(Vector("Error 1", "Error 2"))
  (success1, success2).parMapN(addTwo) // res5: ErrorOr[Int] = Right(3)
}
