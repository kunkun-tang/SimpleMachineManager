package core

import com.twitter.conversions.time._
import com.twitter.finagle._
// import com.twitter.finagle.example._
import com.twitter.finagle.service.{RetryExceptionsFilter, RetryPolicy, TimeoutFilter}
import com.twitter.finagle.thrift.ThriftServiceIface
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Duration, Future, Throw, Try}

object Client extends App{

  val clientServiceIface =
    Thrift.newIface[Grep.FutureIface]("localhost:1234")

  val result: Future[String] = clientServiceIface.grep("liang")
  try{
    Await.result(result, 3.seconds);
	  result.onSuccess { response =>
	    println("Received response: " + response)
	  }
	  result.onFailure { exp =>
	    println("receive from the server: " + exp)
	  }
	}catch{
		case e: Exception => println("hello");
	}
}