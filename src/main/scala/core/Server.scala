package core

import com.twitter.conversions.time._
import com.twitter.finagle._
// import com.twitter.finagle.example._
import com.twitter.finagle.service.{RetryExceptionsFilter, RetryPolicy, TimeoutFilter}
import com.twitter.finagle.thrift.ThriftServiceIface
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Duration, Future, Throw, Try}

object Server extends App{

  val server = Thrift.serveIface(
    "localhost:1234",
    new Grep[Future] {
      def grep(queryStr: String): Future[String] = {
        println(s"Server received: '$queryStr'")
        Future.value(GrepOps.query(queryStr))
      }
  })
  Await.ready(server)
}

object GrepOps{

  import org.grep4j.core.model.Profile
  import org.grep4j.core.model.ProfileBuilder
  import org.grep4j.core.result.GrepResults
  import org.grep4j.core.Grep4j._;
  import org.grep4j.core.Grep4j.constantExpression;
  import org.grep4j.core.fluent.Dictionary.on;

  val localProfile = ProfileBuilder.newBuilder()
                                   .name("Local server log")
                                   .filePath("./first.log")
                                   .onLocalhost()
                                   .build();

  def query(str: String): String = {
    val results: GrepResults = grep(constantExpression(str), localProfile);
    results.toString
  }
}