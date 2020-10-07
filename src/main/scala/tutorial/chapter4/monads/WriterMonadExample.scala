package tutorial.chapter4.monads

import cats.data.Writer
import cats.instances.vector._ // for Monoid
import cats.syntax.applicative._ // for pure

object WriterMonadExample extends App {
  // Creating and Unpacking Writers

  Writer(
    Vector(
      "It was the best of times",
      "it was the worst of times"),
    1859
  )
  // res0: cats.data.WriterT[cats.package.Id, Vector[String], Int] = WriterT(
  // (Vector("It was the best of times", "it was the worst of times"), 1859)
  // )

  type Logged[A] = Writer[Vector[String], A]
  val value: Logged[Int] = 123.pure[Logged] // res1: Logged[Int] = WriterT((Vector(), 123))
  println(value)

  // If we have a log and no result we can create a Writer[Unit] using the tell syntax from cats.syntax.writer

  import cats.syntax.writer._ // for tell

  val tell2: Writer[Vector[String], Unit] = Vector("msg1", "msg2", "msg3").tell
  // res2: Writer[Vector[String], Unit] = WriterT(
  // (Vector("msg1", "msg2", "msg3"), ())
  // )
  println(tell2)

  val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
  // a: cats.data.WriterT[cats.package.Id, Vector[String], Int] = WriterT(
  // (Vector("msg1", "msg2", "msg3"), 123)
  // )
  println(a)
  val aResult: Int = a.value
  val aLog: Vector[String] = a.written // aLog: Vector[String] = Vector("msg1", "msg2", "msg3")

  val b = 123.writer(Vector("msg1", "msg2", "msg3"))
  // b: Writer[Vector[String], Int] = WriterT(
  // (Vector("msg1", "msg2", "msg3"), 123)
  // )
  println(b)
  val (log, result) = b.run
  // log: Vector[String] = Vector("msg1", "msg2", "msg3")
  // result: Int = 123

  // ------- Composing and Transforming Writers -----

  val writer1 = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b

  // writer1: cats.data.WriterT[cats.package.Id, Vector[String], Int] = WriterT(
  //   (Vector("a", "b", "c", "x", "y", "z"), 42)
  // )

  private val run: (Vector[String], Int) = writer1.run // res3: (Vector[String], Int) = (Vector("a", "b", "c", "x", "y", "z") , 42)
  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
  // writer2: cats.data.WriterT[cats.package.Id, Vector[String], Int] = WriterT(
  //   (Vector("A", "B", "C", "X", "Y", "Z"), 42)
  // )

  val writer3 = writer1.bimap(
    log => log.map(_.toUpperCase),
    res => res * 100
  )
  // writer3: cats.data.WriterT[cats.package.Id, Vector[String], Int] = WriterT(
  // (Vector("A", "B", "C", "X", "Y", "Z"), 4200)
  // )


  val writer4 = writer1.mapBoth { (log, res) =>
    val log2 = log.map(_ + "!")
    val res2 = res * 1000
    (log2, res2)
  }

  // writer4: cats.data.WriterT[cats.package.Id, Vector[String], Int] = WriterT(
  // (Vector("a!", "b!", "c!", "x!", "y!", "z!"), 42000)
   // )

  val writer5 = writer1.reset
  // writer5: cats.data.WriterT[cats.package.Id, Vector[String], Int] =WriterT(
  // (Vector(), 42)
  // )

  val writer6 = writer1.swap
  // writer6: cats.data.WriterT[cats.package.Id, Int, Vector[String]] = WriterT(
  //    (42, Vector("a", "b", "c", "x", "y", "z"))
  // )



}
