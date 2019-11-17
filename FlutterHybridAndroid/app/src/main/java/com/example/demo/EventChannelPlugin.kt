package com.example.demo

import io.flutter.plugin.common.EventChannel
import io.flutter.view.FlutterView

class EventChannelPlugin private constructor(flutterView: FlutterView):EventChannel.StreamHandler{
    private var eventSink:EventChannel.EventSink?=null
    companion object{
        fun registerWith(flutterView: FlutterView):EventChannelPlugin{
            val plugin=EventChannelPlugin(flutterView)
            EventChannel(flutterView,"EventChannelPlugin").setStreamHandler(plugin)
            return plugin
        }
    }
    fun send(params:Any){
        eventSink?.success(params)
    }
    override fun onListen(p0: Any?, p1: EventChannel.EventSink?) {
        eventSink=p1
    }

    override fun onCancel(p0: Any?) {
        eventSink=null
    }

}