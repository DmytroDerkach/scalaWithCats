package tutorial.chapter4.monads

import cats.{Id, Monad}
import cats.instances.option._
import cats.instances.list._
import cats.instances.vector._
import cats.instances.future._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.syntax.functor._
import cats.syntax.flatMap._ // for flatMap


object MonadsInCatsExample extends App {
  val opt1 = Monad[Option].pure(3) // opt1: Option[Int] = Some(3)
  val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2)) // opt2: Option[Int] = Some(5)
  val opt3 = Monad[Option].map(opt2)(a => 100 * a) // opt3: Option[Int] = Some(500)
  val list1 = Monad[List].pure(3) // list1: List[Int] = List(3)
  val list2 = Monad[List].
    flatMap(List(1, 2, 3))(a => List(a, a*10)) // list2: List[Int] = List(1, 10, 2, 20, 3, 30)
  val list3 = Monad[List].map(list2)(a => a + 123) // list3: List[Int] = List(124, 133, 125, 143, 126, 153)

  println(opt1)
  println(opt2)
  println(list1)

  // ---------------------------
  val maybeInt: Option[Int] = Monad[Option].flatMap(Option(1))(a => Option(a * 2))  // res0: Option[Int] = Some(2)
  val listInt: List[Int] = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10)) // res1: List[Int] = List(1, 10, 2, 20, 3, 30)

  val vectorInt: Vector[Int] = Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10)) // res2: Vector[Int] = Vector(1, 10, 2, 20, 3, 30)

// ---------------------------
  val fm = Monad[Future]
  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
  val i: Int = Await.result(future, 1.second)
  println(i)
  // ---------------------------
  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = a.flatMap(x => b.map(y => x*x + y*y))

  val maybeInt1: Option[Int] = sumSquare(Option(3), Option(4)) // res7: Option[Int] = Some(25)
  val listInt2: List[Int] = sumSquare(List(1, 2, 3), List(4, 5)) // res8: List[Int] = List(17, 26, 20, 29, 25, 34)
// The Identity Monad: These let us call map, flatMap, and pure passing in plain values:
  sumSquare(3 : Id[Int], 4 : Id[Int]) // res1: Id[Int] = 25

  val a = Monad[Id].pure(3) // a: Id[Int] = 3
  val b = Monad[Id].flatMap(a)(_ + 1) // b: Id[Int] = 4
}
