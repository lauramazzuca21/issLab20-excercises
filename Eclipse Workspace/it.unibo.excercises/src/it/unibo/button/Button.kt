/* Generated by AN DISI Unibo */ 
package it.unibo.button

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Button ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "off"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("off") { //this:State
					action { //it:State
						discardMessages = true
						println("	Button pressed: Turned Off	")
						forward("cmdOff", "cmdOff(1)" ,"led" ) 
					}
					 transition(edgeName="t00",targetState="on",cond=whenEvent("button"))
				}	 
				state("on") { //this:State
					action { //it:State
						println("	Button pressed: Turned On	")
						forward("cmdOn", "cmdOn(1)" ,"led" ) 
					}
					 transition(edgeName="t01",targetState="off",cond=whenEvent("button"))
				}	 
			}
		}
}
