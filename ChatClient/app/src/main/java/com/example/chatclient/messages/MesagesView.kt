package com.example.chatclient.messages

interface MesagesView {
    fun initAdapter(messages: MutableList<MessageModel>)
}