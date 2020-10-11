package tutorial.chapter8.testing_asynchronous_code

import cats.Id
import cats.instances.future._
import cats.instances.list._
import cats.syntax.traverse._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TestingAsynchronousCodeExample extends App {
  trait UptimeClient[F[_]] {
    def getUptime(hostname: String): F[Int]
  }

  /*class UptimeService(client: UptimeClient) {
    def getTotalUptime(hostnames: List[String]): Future[Int] =
      hostnames.traverse(client.getUptime).map(_.sum) }
*/
  /*class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient { def getUptime(hostname: String): Future[Int] =
    Future.successful(hosts.getOrElse(hostname, 0)) }*/
// --------
trait RealUptimeClient extends UptimeClient[Future] {
  def getUptime(hostname: String):  Future[Int]
}

  trait TestUptimeClient extends UptimeClient[Id] {
    def getUptime(hostname: String): Int
  }



  // unit tests for UptimeService.

  def testTotalUptime() = {
    /*val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    assert(actual == expected)*/
  }

}
