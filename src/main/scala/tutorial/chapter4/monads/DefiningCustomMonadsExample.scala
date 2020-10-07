package tutorial.chapter4.monads

import cats.Monad

object DefiningCustomMonadsExample extends App {
  val optionMonad = new Monad[Option]{
    override def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] = opt flatMap fn

    override def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
      fn(a) match {
        case None           => None
        case Some(Left(a1)) => tailRecM(a1)(fn)
        case Some(Right(b)) => Some(b)
      }

    override def pure[A](opt: A): Option[A] = Some(opt)
  }
}
