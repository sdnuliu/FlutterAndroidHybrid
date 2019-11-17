package com.example.demo

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView


class UIPresenter constructor(val activity: Activity,val title:String,val iShowMessage: IShowMessage){
    private var textShow: TextView?=null
    var useEventChannel: Boolean = false
    init {
        initUI()
    }

    private fun initUI() {
        val titleView=activity.findViewById<TextView>(R.id.title)
        titleView.text=title
        val radioGroup=activity.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.mode_basic_message_channel) {
                useEventChannel = false
            } else if (checkedId == R.id.mode_event_channel) {
                useEventChannel = true
            }
        }
        val input=activity.findViewById<EditText>(R.id.input)
        input.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                iShowMessage.sendMessage(s.toString(),useEventChannel)
            }

        })
        textShow=activity.findViewById(R.id.textShow)
    }

    fun showDartMessage(message: String?) {
        textShow?.text = "收到Dart消息:$message"
    }


}