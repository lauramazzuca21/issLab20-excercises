package robotmap
//robotstepperfsm
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import utils.Messages
import kotlinx.coroutines.delay
import fsm.Fsm
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import mapRoomKotlin.mapUtil
import utils.virtualRobotSupportApp

lateinit var robot : Fsm

val stepTime       = 350L	//a step whose space s is the robot length (s=v*stepTime)
val backTime       = 80L	
val pauseStepTime  = 500L	//delay between steps

 	val stepMsg        = AppMsg.create("step",    "main",  "robotstepper" )
	val stepDoneMsg    = AppMsg.create("stepdone","robotstepper",   "main")
	val stepFailMsg    = AppMsg.create("stepfail","robotstepper",   "main")

	val timeMsg        = AppMsg.create("timer", "robotstepper", "timeractor", "${stepTime}")
	val endtimeMsg     = AppMsg.create("endtime", "timeractor", "robotstepper", "expired")

	val endMsg         = AppMsg.buildDispatch("main","end","end","robotstepper")
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class timeractor ( name: String, scope: CoroutineScope ) : Fsm( name, scope){
	
	override fun getInitialState() : String{
		return "init"
	}

	override fun getBody() : (Fsm.() -> Unit){
		return {
			state("init") {
				action {
					println("timeractor init ")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state( "waitcmd" ){
				action {
//					println("timeractor waits ... ")
				}				
				transition( edgeName="t0",targetState="work",    cond=whenDispatch("timer") ) 
				transition( edgeName="t0",targetState="endwork", cond=whenDispatch("end") ) 
			}
			
			state("work") {
				action {
					 val delayTime = currentMsg.CONTENT.toLong()
//					 println("timeractor work $delayTime")
					 delay( delayTime )
					 Messages.forward( endtimeMsg, robot  )					
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() ) 
			}				
			state("endwork") {
				action {
					println("			timer ENDS")
 					terminate()
  				}
 			}									
		}//return
	}//getBody
}//timeractor



@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class robotmapper ( name: String, scope: CoroutineScope, discardMessages:Boolean=true  ) : Fsm( name, scope, discardMessages ){
lateinit var timer : Fsm
var stepCounter = 0
	
	override fun getInitialState() : String{
		return "init"
	}

	suspend fun doMove( move: String ){
	  	virtualRobotSupportApp.doApplMove( move )		//move in the application-language
	}
		
	override fun getBody() : (Fsm.() -> Unit){
		return {
			state("init") {
				action {  
					println("robotmapper init ")
					virtualRobotSupportApp.initClientConn()
					timer = timeractor( "timer", scope)
					mapRoomKotlin.buildRefTestMap()
					val refTestMap = mapUtil.getMapAndClean()
			 		println( refTestMap )
					println("-----------------")
					mapUtil.showMap()
				}
				transition( edgeName="t0",targetState="work", cond=doswitch() )			//empty move	
			}
			state("work")	{
				action {
					//println("robotmapper waits ... ")
				}				
				transition( edgeName="t0",targetState="doStep",  cond=whenDispatch("step") )	
				transition( edgeName="t1",targetState="endwork", cond=whenDispatch("end")  )	
			}
			state("doStep") {
				action {
					doMove("w")
 					Messages.forward( timeMsg, timer  )
				}
				transition( edgeName="t2",targetState="stepKo", cond=whenDispatch("sensor")  )   	//(first)
				transition( edgeName="t1",targetState="stepOk", cond=whenDispatch("endtime") )
				transition( edgeName="t3",targetState="endwork", cond=whenDispatch("end") )
			}
			state("stepOk") {
				action {
					doMove("h")
 					stepCounter++
					println("			robotmapper stepCounter=$stepCounter")
					mapUtil.doMove("w")
					mapUtil.showMap()
					//send stepDoneMsg to the caller
 				}
				transition( edgeName="t0",targetState="work", cond=doswitch() )				
 			}			
			state("stepKo") {
				action {
					println("robotmapper stepKo")
					//send stepFailMsg to the caller
   				}
				transition( edgeName="t0",targetState="consumeEndTime", cond=whenDispatch("endtime") )
			}	//WARNING: the msg timer IS NOT LOST: it should be consumed
			
			state("consumeEndTime"){
				action {
					println("robotmapper consume endtime")
   				}
				transition( edgeName="t0",targetState="endwork", cond=doswitch() )				
			}
									
			state("endwork") {
				action {
					println("			robotmapper ENDS")
					Messages.forward( endMsg, timer  )
 					terminate()
  				}
 			}	 							
		}//return
	}//getBody	
}//robotmapper


@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main()=runBlocking{
	println("main STARTS")
	
	robot = robotmapper("robotmapper", this )
  	virtualRobotSupportApp.setRobotTarget( robot   ) //Configure - Inject
	
	delay( 100 )
	
	for( i in 1..8 ){	
		delay(1000)
		Messages.forward( stepMsg, robot  )
	}
	
 	Messages.forward( endMsg, robot  )
	
	(robot.fsmactor as Job).join()	//waits for termination  
	
	println(			"main ENDS")
}