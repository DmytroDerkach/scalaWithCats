package tutorial.chapter3.functors
import cats.instances.function._ // for Functor
import cats.syntax.functor._ // for map


object FunctorsExample extends App {
  val func1: Int => Double = (x: Int) => x.toDouble
  val func2: Double => Double = (y: Double) => y * 2
  (func1 map func2)(1) // composition using map
  // res3: Double = 2.0 // composition using map
  (func1 andThen func2)(1) // composition using andThen
  // res4: Double = 2.0 // composition using andThen
  func2(func1(1)) // composition written out by hand
  // res5: Double = 2.0

  val func = ((x: Int) => x.toDouble)
    .map(x => x + 1)
    .map(x => x * 2)
    .map(x => s"${x}!")
  func(123)
  // res6: String = "248.0!"
}
