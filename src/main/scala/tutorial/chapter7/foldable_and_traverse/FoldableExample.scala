package tutorial.chapter7.foldable_and_traverse

import cats.instances.option._ // for Foldable
import cats.Foldable
import cats.instances.list._ // for Foldable
import cats.Eval
import cats.instances.lazyList._ // for Foldable
import cats.instances.int._ // for Monoid
import cats.instances.string._ // for Monoid
import cats.instances.vector._ // for Monoid

object FoldableExample extends App {

  // FOLD
  // We supply an accumulator value and a binary function to combine it with each item in the sequence:
  def show[A](list: List[A]): String = list.foldLeft("nil")((accum, item) => s"$item then $accum")

  show(Nil) // res0: String = "nil"
  show(List(1, 2, 3)) // res1: String = "3 then 2 then 1 then nil"


  private val foldL: Int = List(1, 2, 3).foldLeft(0)(_ - _) // res4: Int = -6 [0-3-2-1]
  println(foldL)
  private val foldR: Int = List(1, 2, 3).foldRight(0) {
    (accum, item) =>
      println(s"$accum-$item")
      accum - item
  } // res5: Int = 2 [1-(2-(3-0))]
  println(foldR)

  // Foldable Cats


  val ints = List(1, 2, 3)
  Foldable[List].foldLeft(ints, 0)(_ + _) // res0: Int = 6

  val maybeInt = Option(123)
  private val i: Int = Foldable[Option].foldLeft(maybeInt, 10)(_ * _) // res1: Int = 1230

  // ------------
  def bigData = (1 to 100000).to(LazyList)

  private val l: Long = bigData.foldRight(0L)(_ + _)
  println(l)
  val eval: Eval[Long] = Foldable[LazyList].
    foldRight(bigData, Eval.now(0L)) { (num, eval) => eval.map(_ + num)
    }
  println(eval.value)
  //-------------
  Foldable[Option].nonEmpty(Option(42)) // res6: Boolean = true
  Foldable[List].find(List(1, 2, 3))(_ % 2 == 0) // res7: Option[Int] = Some(2)
  Foldable[List].combineAll(List(1, 2, 3))// res8: Int = 6
  Foldable[List].foldMap(List(1, 2, 3))(_.toString) // res9: String = "123"

  val ints2 = List(Vector(1, 2, 3), Vector(4, 5, 6))
  (Foldable[List] compose Foldable[Vector]).combineAll(ints2)// res11: Int = 21

}
