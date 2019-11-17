package com.example.demo

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import io.flutter.facade.Flutter
import io.flutter.view.FlutterView


class NextActivity : FragmentActivity(),IShowMessage{
    private var uiPresenter: UIPresenter? = null
    private var basicMessageChannelPlugin: BasicMessageChannelPlugin? = null
    private var eventChannelPlugin: EventChannelPlugin? = null
    override fun onShowMessage(message: String?) {
        uiPresenter?.showDartMessage(message)
    }

    override fun sendMessage(message: String, useEventChannel: Boolean) {
        if (useEventChannel) {
            eventChannelPlugin?.send(message)
        } else {
            basicMessageChannelPlugin?.send(message, basicMessageChannelPlugin)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        val params=intent.getStringExtra("params")
        val ft= supportFragmentManager.beginTransaction()
        val fragment=Flutter.createFragment(params)
        ft.replace(R.id.fl_container,fragment)
        ft.commit()
        Handler(mainLooper).postDelayed({
            eventChannelPlugin = EventChannelPlugin.registerWith(fragment.view as FlutterView)
            //注册Flutter plugin
            MethodChannelPlugin.registerWith(fragment.view as FlutterView)
            basicMessageChannelPlugin = BasicMessageChannelPlugin.registerWith(fragment.view as FlutterView)
            uiPresenter = UIPresenter(this, "通信与混合开发", this)
        },1000)
    }
}
