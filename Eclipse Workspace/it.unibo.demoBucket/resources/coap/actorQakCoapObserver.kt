package coap.observer

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 

object ch : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("actorQakCoapObserver chhhhhhhhh | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("actorQakCoapObserver chhhhhhhhh | FAILED")
            }
        } 
 
object actorQakCoapObserver {

    private val client = CoapClient()
    
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( context: String, destactor: String, ipaddr : String , owner: ActorBasic? = null){ 
       val uriStr = "$ipaddr/$context/$destactor"
 	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
	   client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
	   client.observe( ch )
      
    }

 }

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) { //82.56.16.191
		actorQakCoapObserver.activate("ctxledalone", "led", "localhost:8080" )
		System.`in`.read()   //to avoid exit
 }

