package com.example.demo

interface IShowMessage{
         fun onShowMessage(message: String?)
         fun sendMessage(message: String, useEventChannel: Boolean)
}