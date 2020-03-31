package demoDSL

import kotlinx.coroutines.CoroutineScope
import fsm.Fsm
import kotlinx.coroutines.runBlocking
import utils.Messages
import utils.AppMsg
import kotlinx.coroutines.delay

class demoDSL ( name: String, scope: CoroutineScope ) : Fsm( name, scope){
	
override fun getInitialState() : String{
 return "s0"
 }

override fun getBody() : (Fsm.() -> Unit){
  return {
	state("s0") {
		action {   println("demoDSL | state s0 $currentMsg")  }
		transition( edgeName="t0",
			targetState="s1", cond=doswitch())	 //empty move		
	}			
	state("s1") {
		action {   println("demoDSL | state s1 $currentMsg")  }
		transition( edgeName="t1",
			targetState="s2",   cond=whenDispatch("msg1") )
		transition( edgeName="t2",
			targetState="s3", cond=whenDispatch("msg2") )
	}
	state("s2") {
		action {   println("demoDSL | state s2 $currentMsg")  }
		transition( edgeName="t0",
			targetState="s3", cond=whenDispatch("msg2") )
	}
	state("s3") {
		action {   println("demoDSL | state s3 $currentMsg")  }
		transition( edgeName="t0",
			targetState="s1", cond=doswitch())	 //empty move
	}
	}//return
}//getBody 
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() = runBlocking(){
	val demo = demoDSL( "demo", this )
 	delay( 100 )
	Messages.forward("main","msg1","msg1_hello",  demo)
	delay( 100 )
	Messages.forward("main","msg1","msg1_hello", demo)
	delay( 100 )
	Messages.forward("main","msg2","msg2_hello", demo)
	
}