package virtualRobotUsage.boundaryWalker
//robotActorUsage.kt

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import virtualRobotUsage.AppMsg
import virtualRobotUsage.AppMsgType
import virtualRobotUsage.virtualRobotSupport

val initMsg         = AppMsg.create("init","main","robotactor")
val endMsg          = AppMsg.create("end", "main","robotactor")
val startMsg  		= AppMsg.create("move","main","robotactor","w") 
val resumeMsg 		= AppMsg.create("move","main","robotactor","w") 
val stopMsg    		= AppMsg.create("move","main","robotactor","h") 

enum class State{ menu, handlingMovingRobot }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun forward(  msg : AppMsg ){
 	RobotActor.send( msg.toString()  )
}
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendCommands(   ) {
	var state = State.menu
	virtualRobotSupport.setRobotTarget( RobotActor  ) //Configure - Inject

	//init RobotActor
	forward( initMsg )
	//show user start and end commands usage and wait for acceptable command
	var input = readLine()!!		
	printUsage(state)
    while (!input.isNullOrBlank()) {
		if (state == State.menu) {
			when (input){
				"start"    -> {forward(startMsg); state = State.handlingMovingRobot}
	 			"end"      -> { forward(endMsg); return }
				else        -> printUsage(state)
	
			}
		} else {
			when (input){
				"resume"    -> forward(resumeMsg)
	 			"stop"      -> forward(stopMsg)
				else        -> printUsage(state)
			}

		}
		input = readLine()!!
	}
}

fun printUsage(state:State) {
    println("Please, write:")
	when(state) {
		State.menu -> {	println("'start' to begin execution;")
						println("'end' to exit;")}
		State.handlingMovingRobot -> {
			println("'stop' if robot is moving and you want to stop it;")
			println("'resume' if you want to resume robot movement after stop.")
		}
	}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
	sendCommands(   )
    println("BYE")
}

