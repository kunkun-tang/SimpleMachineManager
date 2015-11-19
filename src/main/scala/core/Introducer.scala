package core

import com.twitter.conversions.time._
import com.twitter.finagle._
// import com.twitter.finagle.example._
import com.twitter.finagle.thrift.ThriftServiceIface
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Duration, Future, Throw, Try}

import scala.collection.mutable.{Set, HashSet};

object Introducer extends App{

  val addrSet = new HashSet[String]();

  val server = Thrift.serveIface(
    "localhost:1234",
    new MemberShip[Future] {
      def join(joinMsg: String)= {
        println(s"Server received: '$joinMsg'")
        addrSet += joinMsg;
        Future.value()
      }
      def leave(leaveMeg: String)= {
        println(s"Server received: '$leaveMeg'")
        addrSet -= leaveMeg
        Future.value()
      }
      def ping(pingStr: String): Future[String] = {
        throw new Exception("can not receive ping")
      }
  })

  val rnd=new scala.util.Random
  while(true){
    Thread.sleep(200);
    println("addrSet size =" + addrSet.size);
    if(addrSet.size > 0){
      val elem = addrSet.toVector(rnd.nextInt(addrSet.size));
      val clientServiceIface =
        Thrift.newIface[MemberShip.FutureIface](elem)
      val result: Future[String] = clientServiceIface.ping("pinging from Introducer");

      try{
        Await.result(result, 3.seconds);
        result.onFailure { exp =>
          println("some Exception Happens: " + exp)
        }
        result.onSuccess { pingMeg =>
          println("ping Message:" + pingMeg);
        }
      }catch{
        case e: Exception => println("can not ping");
      }
    }
  }

  Await.ready(server)


  // def sendRmMsg(rmAddr: String)={
  //   for(addr <- addrSet){
  //     val clientServiceIface =
  //       Thrift.newIface[MemberShip.FutureIface](addr)
  //     val result: Future[String] = clientServiceIface.remove(rmAddr);
  //     try{
  //       Await.result(result, 3.seconds);
  //       result.onFailure { exp =>
  //         println("some Exception Happens: " + exp)
  //       }
  //       result.onSuccess { pingMeg =>
  //         println("Remove SuccesFul.");
  //       }
  //     }catch{
  //       case e: Exception => println("can not connect to the current machine.");
  //     }

  //   }
  // }
}
