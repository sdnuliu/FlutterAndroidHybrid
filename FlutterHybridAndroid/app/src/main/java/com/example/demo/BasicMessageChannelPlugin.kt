package com.example.demo
import android.app.Activity
import android.widget.Toast
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.StringCodec
import io.flutter.view.FlutterView

class BasicMessageChannelPlugin constructor(flutterView:FlutterView):BasicMessageChannel.MessageHandler<String>,BasicMessageChannel.Reply<String>{
    private val activity=flutterView.context as Activity
    private val messageChannel=BasicMessageChannel(flutterView,"BasicMessageChannelPlugin",StringCodec.INSTANCE)
    init {
        messageChannel.setMessageHandler(this)
    }
    companion object{
        fun registerWith(flutterView: FlutterView):BasicMessageChannelPlugin{
            return BasicMessageChannelPlugin(flutterView)
        }
    }
    override fun onMessage(receivedMsg: String?,androidReply: BasicMessageChannel.Reply<String>) {
        androidReply.reply("Android收到了：$receivedMsg")
        if (activity is IShowMessage){
            activity.onShowMessage(receivedMsg)
        }
        Toast.makeText(activity,receivedMsg,Toast.LENGTH_LONG).show()
    }

    fun send(message:String,callback:BasicMessageChannel.Reply<String>?) {
        messageChannel.send(message,callback)
    }

    override fun reply(p0: String?) {

    }

}