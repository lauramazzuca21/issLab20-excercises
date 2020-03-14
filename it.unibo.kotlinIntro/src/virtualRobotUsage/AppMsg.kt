package virtualRobotUsage
//AppMessage.kt
enum class AppMsgType{ event, dispatch, request, reply, invitation }

open class AppMsg( val MSGID: String, val MSGTYPE: String,
					   val SENDER: String, val RECEIVER: String,
                       val CONTENT: String, val SEQNUM: String  )  {
  companion object {
	  private var n = 0;
	  fun create(MSGID: String, SENDER: String, RECEIVER: String, CONTENT: String="none", 
                 MSGTYPE: String=AppMsgType.dispatch.toString() ) : AppMsg{
		  return AppMsg(MSGID,MSGTYPE,SENDER,RECEIVER,CONTENT,"${n++}")
	  }
	  fun create( msg: String ) : AppMsg{
		  val msgSplitted = msg.split('(')
		  val msgBody     = msgSplitted[1].split(',')
		  val msgId       = msgBody[0]
		  val msgType     = msgSplitted[1]
		  val sender      = msgBody[2]
		  val receiver    = msgBody[3]
 		  val content     = msgBody[4]
		  val msgNum      = msgBody[5].replace(")","")
		  return AppMsg(msgId,msgType,sender,receiver,content,msgNum)
	  }
  }

  override fun toString(): String {
	  //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )	
	  return "msg($MSGID,$MSGTYPE,$SENDER,$RECEIVER,$CONTENT,${SEQNUM})"
  }

  fun isEvent(): Boolean{ return MSGTYPE == AppMsgType.event.toString()       }
  fun isDispatch(): Boolean{ return MSGTYPE == AppMsgType.dispatch.toString() }
  fun isRequest(): Boolean{ return MSGTYPE == AppMsgType.request.toString()   }
  fun isReply(): Boolean{ return MSGTYPE == AppMsgType.reply.toString()       }
}//AppMessage