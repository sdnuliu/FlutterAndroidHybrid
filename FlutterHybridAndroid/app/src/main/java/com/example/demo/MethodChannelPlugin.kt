package com.example.demo

import android.app.Activity
import android.widget.Toast
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterView

class MethodChannelPlugin private constructor( activity: Activity):MethodChannel.MethodCallHandler{
    private val mActivity=activity
    companion object{
        fun registerWith(flutterView: FlutterView){
            val methodChannel=MethodChannel(flutterView,"MethodChannelPlugin")
            val instance=MethodChannelPlugin(flutterView.context as Activity)
            methodChannel.setMethodCallHandler(instance)
        }
    }
    override fun onMethodCall(p0: MethodCall, p1: MethodChannel.Result) {
        when(p0.method){
            "send"->{
                showMessage(p0.arguments)
                p1.success("MethodChannelPlugin收到：" + p0.arguments);//返回结果给Dart
            }else->{

        }
        }
    }

    private fun showMessage(arguments: Any?) {
        if (mActivity is IShowMessage){
            mActivity.onShowMessage(arguments.toString())
        }
        Toast.makeText(mActivity,arguments.toString(),Toast.LENGTH_LONG).show()
    }

}