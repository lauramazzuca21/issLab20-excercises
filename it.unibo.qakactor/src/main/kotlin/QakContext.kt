package it.unibo.kactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.InetAddress
import org.eclipse.californium.core.CoapServer
import kotlinx.coroutines.runBlocking

open class QakContext(name: String, val hostAddr: String, val portNum: Int, var mqttAddr : String = "",
                      val external: Boolean=false, val gui : Boolean = false   ) : ActorBasic(name){

    internal val actorMap : MutableMap<String, ActorBasic> = mutableMapOf<String, ActorBasic>()
    internal val proxyMap:  MutableMap<String, NodeProxy> = mutableMapOf<String, NodeProxy>()  //cannot be static
    //lateinit private var serverCoap  :  CoapServer      //CoAP: Jan2020
    private var resourceCtx : CoapResourceCtx
	lateinit var ctxserver  : QakContextServer
	lateinit var serverCoap : CoapServer
	
    companion object {
        val workTime = 600000L		 
        enum class CtxMsg { attach, remove }

        fun getActor( actorName : String ) : ActorBasic? {
            return sysUtil.getActor(actorName)
        }
//Called by genrated code main of ctx
        suspend fun createContexts(hostName: String, scope: CoroutineScope ,
                           desrFilePath: String, rulesFilePath: String) {
            sysUtil.createContexts(hostName, desrFilePath, rulesFilePath)

            if( sysUtil.ctxOnHost.size == 0 ){
                val ip = InetAddress.getLocalHost().getHostAddress()
                sysUtil.traceprintln("               %%% QakContext | CREATING NO ACTORS on $hostName ip=${ip.toString()}")
            }
            else println("               %%% QakContext | CREATING THE ACTORS on $hostName ")
//			runBlocking {
            sysUtil.ctxOnHost.forEach { ctx -> sysUtil.createTheActors(ctx, scope)  }
            //Avoid premature termination
//            scope.launch{
//                println("               %%% QakContext |  $hostName CREATED. I will terminate after $workTime msec")
//                delay( workTime )
//            }
//			(scope as Job).join()
//			}
 			println("               %%% QakContext | createContexts on $hostName ENDS " )
        }
    }

    init{
        //OCT2019 --> NOV2019 Create a QakContextServer also when we use MQTT
        resourceCtx = CoapResourceCtx( name, this )   //must be ininitialized here
        if( ! external ){
            println("               %%% QakContext |  $hostAddr:$portNum INIT ")
            ctxserver = QakContextServer( this, GlobalScope, "server$name", Protocol.TCP )
            //CoAP: Jan2020
              try{
                  val coapPort    =  portNum
                  serverCoap      =  CoapServer(coapPort)
                  serverCoap.add(  resourceCtx )
                  serverCoap.start()
                  println( "               %%% QakContext $name |  serverCoap started on port: $coapPort" )
            }catch(e : Exception){
                println( "               %%% QakContext $name |  serverCoap error: ${e.message}" )
            }
         }
     }
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun terminateTheContext(){
		serverCoap.stop()
		ctxserver.actor.close()
	}

    override suspend fun actorBody(msg : ApplMessage){
        sysUtil.traceprintln( "               %%% QakContext $name |  receives $msg " )
    }

    fun addCtxProxy(ctxName: String, protocol: String, hostAddr: String, portNumStr: String) {
        val p = MsgUtil.strToProtocol(protocol)
        val portNum = Integer.parseInt(portNumStr)
        //sysUtil.traceprintln("               %%% QakContext $name | addCtxProxy ${ctxName}, $hostAddr, $portNum")\
        val proxy = NodeProxy("proxy${ctxName}", this, p, hostAddr, portNum)
        proxyMap.put(ctxName, proxy)
    }

    fun addActor( actor: ActorBasic ) {
        actor.context = this    //injects the context
        actorMap.put( actor.name, actor )
        actor.checkMqtt()
        //sysUtil.traceprintln("               %%% QakContext $name | addActor ${actor.name}")
        resourceCtx.addActorResource( actor )             //CoAP: Jan2020
    }

    fun addInternalActor( actor: ActorBasic ) {
        actor.context = this    //injects the context
        actorMap.put( actor.name, actor )
    }

    fun removeInternalActor( actor: ActorBasic ){
        actorMap.remove( actor.name )
     }

    fun hasActor( actorName: String ) : ActorBasic? {
        return actorMap.get(actorName)
    }

    fun addCtxProxy( ctx : QakContext ){
        if( ctx.mqttAddr.length > 1 ) return
        //sysUtil.traceprintln("               %%% QakContext $name | addCtxProxy ${ctx.name}")
        val proxy = NodeProxy("proxy${ctx.name}", this, Protocol.TCP, ctx.hostAddr, ctx.portNum)
        proxyMap.put( ctx.name, proxy )
    }

    fun addCtxProxy( ctxName :String, hostAddr: String, portNum : Int  ){
        val proxy = NodeProxy("proxy${ctxName}", this, Protocol.TCP, hostAddr, portNum)
        proxyMap.put( ctxName, proxy )
    }

}