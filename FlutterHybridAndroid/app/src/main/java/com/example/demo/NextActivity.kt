package com.example.demo

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.flutter.facade.Flutter

class NextActivity : FragmentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        val ft= supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_container,Flutter.createFragment("{key:helloword}"))
        ft.commit()
    }
}
