package robotAppl

import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import fsm.Fsm
import utils.virtualRobotSupportApp
import utils.Messages
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class sensorObserver ( name: String, scope: CoroutineScope,
				   usemqtt:Boolean=true,  discardMessages:Boolean=true
				 ) : Fsm( name, scope, discardMessages,usemqtt){
	
	override fun getInitialState() : String{
		return "init"
	}

	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
					println("$ndnt sensorObserver | STARTED ")
				}
				transition( edgeName="t0",targetState="waitevents", cond=doswitch() )
			}
			state("waitevents") {	
				action { //it:State
					//println("$ndnt sensorObserver | WAITS ")
 				}
				transition( edgeName="t0",targetState="handleevent", cond=whenEvent("sensor") )
			}
			state("handleevent") {	
				action { //it:State
					println("$ndnt sensorObserver | handles $currentMsg ")
 				}
				transition( edgeName="t0",targetState="waitevents", cond=doswitch() )
			}
		}
	}
}//sensorObserver

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	val sobs = sensorObserver("sensorobserver", this )
 	
 	sobs.waitTermination()
	println("main ends")
}