/* Generated by AN DISI Unibo */ 
package it.unibo.demostrange

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Demostrange ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("demostrange | s0")
						forward("cmd", "cmd(a)" ,"demostrange" ) 
						stateTimer = TimerActor("timer_s0", 
							scope, context!!, "local_tout_demostrange_s0", 1000.toLong() )
					}
					 transition(edgeName="t00",targetState="s1",cond=whenTimeout("local_tout_demostrange_s0"))   
					transition(edgeName="t01",targetState="s2",cond=whenDispatch("cmd"))
					transition( edgeName="goto",targetState="s1", cond=doswitch() )
				}	 
				state("s1") { //this:State
					action { //it:State
						println("demostrange | s1")
						forward("cmd", "cmd(b)" ,"demostrange" ) 
					}
					 transition(edgeName="t02",targetState="s2",cond=whenDispatch("cmd"))
				}	 
				state("s2") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("demostrange | s2, BYE")
					}
					 transition(edgeName="t03",targetState="s2",cond=whenDispatch("cmd"))
					transition( edgeName="goto",targetState="s3", cond=doswitch() )
				}	 
				state("s3") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("demostrange | s3, BYE")
					}
				}	 
			}
		}
}
