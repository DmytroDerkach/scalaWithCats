import cats.Semigroup
import cats.implicits._

Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
Semigroup[Option[Int]].combine(Option(1), Option(2))
Semigroup[Option[Int]].combine(Option(1), None)


Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6)

val aMap = Map("foo" -> Map("bar" -> 5))
val anotherMap = Map("foo" -> Map("bar" -> 6))
val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(aMap, anotherMap)
combinedMap.get("foo")


var rec = 0
def llRange(lo: Int, hi: Int): LazyList[Int] = {
  rec = rec + 1
  if (lo >= hi) LazyList.empty
  else LazyList.cons(lo, llRange(lo + 1, hi))
}
llRange(1, 10).take(3).toList

rec

def calc(x: () => Int): Either[Throwable, Int] = {
  try Right(x()) // An explicit call of the x function
  catch {
    case b: Throwable => Left(b)
  }
}

val y = calc { () => // explicitly declaring that Unit is a parameter with ()
  14 + 15
}

val y2 = calc { () =>
  // This looks like a natural block
  println("Here we go!") // Some superfluous call
  val z = List(1, 2, 3, 4) // Another superfluous call
  49 + 20
}

def repeatedParameterMethod(x: Int, y: String, z: Any*) = {
  "%d %ss can give you %s".format(x, y, z.mkString(", "))
}

repeatedParameterMethod(
  3,
  "egg",
  List("a delicious sandwich", "protein", "high cholesterol"): _*)