package core

import com.twitter.conversions.time._
import com.twitter.finagle._
// import com.twitter.finagle.example._
import com.twitter.finagle.service.{RetryExceptionsFilter, RetryPolicy, TimeoutFilter}
import com.twitter.finagle.thrift.ThriftServiceIface
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Duration, Future, Throw, Try}
import scala.collection.mutable.{Set, HashSet};

object Machine extends App{

  val clientServiceIface =
    Thrift.newIface[MemberShip.FutureIface]("localhost:1234")
  val localAddr = "localhost:9001";
  val result: Future[Unit] = clientServiceIface.join(localAddr)
  try{
    Await.result(result, 2.seconds);
	  result.onFailure { exp =>
	    println("can not join: " + exp)
	  }
	}catch{
		case e: Exception => println("can not join");
	}


  val machineServer = Thrift.serveIface(
    "localhost:9001",
    new MemberShip[Future] {
      def join(joinMsg: String)= throw new Exception("can not join")
      def leave(leaveMeg: String)= throw new Exception("can not leave");
      def ping(pingStr: String): Future[String] = {
        println(s"local received ping messages: '$pingStr'")
        Future.value(localAddr);
      }
  })
	println("joining succesfully")
  val addrSet = HashSet[String]();

  while(true){
    Thread.sleep(1000);
    println("machine set =" + addrSet);
  }

  Await.ready(machineServer)
}