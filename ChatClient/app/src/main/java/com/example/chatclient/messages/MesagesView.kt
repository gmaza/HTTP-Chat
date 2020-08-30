package com.example.chatclient.messages

interface MesagesView {
    fun initAdapter(messages: MutableList<MessageModel>)

    fun notifiAdapter()

    fun showToast(message: String)
}