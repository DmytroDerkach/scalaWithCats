package tutorial.chapter5.monad_transformers

import cats.implicits.{catsStdInstancesForList, catsSyntaxApplicativeId}
import cats.instances.either._ // for Monad

import scala.concurrent.Future
import cats.data.{EitherT, OptionT}
import cats.instances.future._ // for Monad
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object TransformativeExample extends App {

  type ListOption[A] = OptionT[List, A]

  val result1: ListOption[Int] = OptionT(List(Option(10))) // result1: ListOption[Int] = OptionT(List(Some(10)))
  val result2: ListOption[Int] = 32.pure[ListOption]


  // -----------------------
  // Alias Either to a type constructor with one parameter:
  type ErrorOr[A] = Either[String, A]
  // Build our final monad stack using OptionT:
  type ErrorOrOption[A] = OptionT[ErrorOr, A]

  val a = 10.pure[ErrorOrOption] // a: ErrorOrOption[Int] = OptionT(Right(Some(10)))
  val b = 32.pure[ErrorOrOption] // b: ErrorOrOption[Int] = OptionT(Right(Some(32)))

  val c = a.flatMap(x => b.map(y => x + y)) // c: OptionT[ErrorOr, Int] = OptionT(Right(Some(42)))
  println(c)

  //--------------------------
  // an alias for EitherT that fixes Future and Error and allows A to vary:
  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]


  val futureEitherOr: FutureEitherOption[Int] =
    for {
      a <- 10.pure[FutureEitherOption]
      b <- 32.pure[FutureEitherOption]
    } yield a + b

  println(futureEitherOr) // res6: FutureEitherOption[Int] = OptionT( EitherT(Future(Success(Right(Some(42)))))  )

  val intermediate = futureEitherOr.value
  // intermediate: FutureEither[Option[Int]] = EitherT(
  // Future(Success(Right(Some(42))))
  // )
  val stack = intermediate.value
  // stack: Future[Either[String, Option[Int]]] = Future(Success(Right(Some(42))))
  Await.result(stack, 1.second)
  // res7: Either[String, Option[Int]] = Right(Some(42))

  // ------------------ Constructing and Unpacking Instances

  // Create using apply:
  val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10))) // errorStack1: OptionT[ErrorOr, Int] = OptionT(Right(Some(10)))

  // Create using pure:
  val errorStack2 = 32.pure[ErrorOrOption] // errorStack2: ErrorOrOption[Int] = OptionT(Right(Some(32)))

  // Extracting the untransformed monad stack:
  private val value: ErrorOr[Option[Int]] = errorStack1.value // res4: ErrorOr[Option[Int]] = Right(Some(10))
  // Mapping over the Either in the stack:
  private val stringOrInt: Either[String, Int] = errorStack2.value.map(_.getOrElse(-1)) // res5: Either[String, Int] = Right(32)
}
