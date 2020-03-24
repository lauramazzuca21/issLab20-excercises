package virtualRobotUsage.boundaryWalker
//robotActor.kt

import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import virtualRobotUsage.virtualRobotSupport
import virtualRobotUsage.AppMsg
  
//Actor that includes the business logic; the behavior is message-driven 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val RobotActor  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
	var state    = "working"
	val homeWall = "wallLeft"
	fun doInit() = virtualRobotSupport.initClientConn() 
	fun doEnd()  = { state = "end"  }
		
	suspend fun doCollision(msg : AppMsg){
//		println("robotActor handles ${msg.CONTENT} by going back a little");
		virtualRobotSupport.doApplMove( "l" ) //turn left (moving counterclockwise)
		if (!msg.CONTENT.contains(homeWall)) { //only when it's been hit a different wall from the homewall we keep going
			delay(100)		
			virtualRobotSupport.doApplMove( "w" )
		}
	}
	suspend fun doSensor(msg : virtualRobotUsage.AppMsg){
		println("robotActor handles: ${msg.CONTENT}")
		if( msg.CONTENT.startsWith("collision") ) doCollision(msg)
	}
	
	suspend fun doMove( move: String ){
  		virtualRobotSupport.doApplMove( move )		//move in the application-language 
	}
	
	while( state == "working" ){
		var msg = channel.receive()
		println("robotActor receives: $msg ")
		val applMsg = AppMsg.create(msg)
 		//println("robotActor applMsg.MSGID=${applMsg.MSGID} ")
		when( applMsg.MSGID ){
			"init"      -> doInit()
			"end"       -> doEnd()
			"sensor"    -> doSensor(applMsg)
 			"move"      -> doMove(applMsg.CONTENT)
			else        -> println("NO HANDLE for $msg")
		}		
 	}//while
 	println("robotActor ENDS state=$state")
}

 

