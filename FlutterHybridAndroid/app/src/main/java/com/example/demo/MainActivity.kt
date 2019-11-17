package com.example.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val inputEt=findViewById<EditText>(R.id.editText2)
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            val intent=Intent(this@MainActivity,NextActivity::class.java)
            intent.putExtra("params",inputEt.text.toString())
            startActivity(intent)
        }
    }
}
