package tutorial.chapter4.monads

import cats.Id
import cats.data.Reader

object ReaderMonadExample extends App {
  final case class Cat(name: String, favoriteFood: String)

  val catName: Reader[Cat, String] = Reader(cat => cat.name)
  // catName: Reader[Cat, String] = Kleisli(<function1>)
  val value: Id[String] = catName.run(Cat("Garfield", "lasagne")) // res1: cats.package.Id[String] = "Garfield"
  println(value)
}
