package tutorial.chapter4.monads

import cats.Eval

object EvalMonadExample extends App {
// Scala vals
  /*val x = { println("Computing X")
      math.random
    }
    // Computing X
    // x: Double = 0.0396922778013471
    // first access
    // res0: Double = 0.0396922778013471 // first access
    // second access
    // res1: Double = 0.0396922778013471
  /*
  This is an example of call‐by‐value evaluation:
  • the computation is evaluated at point where it is defined (eager); and
  • the computation is evaluated once (memoized).

   */

  def y = { println("Computing Y")
    math.random
  }
   // first access
  // Computing Y
  // res2: Double = 0.5270290953284378 // first access
   // second access
  // Computing Y
  // res3: Double = 0.348549829974959
  /*
  These are the properties of call‐by‐name evaluation:
• the computation is evaluated at the point of use(lazy); and
• the computation is evaluated each time it is used(not memoized).
   */

  lazy val z = { println("Computing Z")
    math.random
  }
   // first access
  // Computing Z
  // res4: Double = 0.6672110951657263 // first access
   // second access
  // res5: Double = 0.6672110951657263

  /*
  lazy vals are an example of call‐by‐need evaluation
  The code to compute z below is not run until we use it for the first time (lazy).
  The result is then cached and re‐used on subsequent accesses (memoized):
   */

*/

  // -------- cats
  // call‐by‐value : val
  // val now = Eval.now(math.random + 1000) // now: Eval[Double] = Now(1000.7009661848473)
  //now.value
  // call‐by‐name: def
  // val always = Eval.always(math.random + 3000) // always: Eval[Double] = cats.Always@2a4e7955
  //always.value
  // call‐by‐need: lazy val
  // val later = Eval.later(math.random + 2000) // later: Eval[Double] = cats.Later@7684da18
  //later.value

  //---- Eval as a Monad
  val greeting = Eval
    .always{
      println("Step 1");
      "Hello"
    }
    .map{
      str =>
        println("Step 2");
        s"$str world"
    }
  // greeting: Eval[String] = cats.Eval$$anon$4@496b9f25
  println(greeting.value)
  // Step 1
  // Step 2
  // res16: String = "Hello world"
}
