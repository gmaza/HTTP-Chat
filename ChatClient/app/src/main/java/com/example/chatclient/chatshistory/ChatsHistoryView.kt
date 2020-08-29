package com.example.chatclient.chatshistory

interface ChatsHistoryView {
    fun setAdapter(quantity: Int, searchMode: Boolean, users: MutableList<UserModel>,  histories: MutableList<HistoryModel>)

    fun showEmptyScreen()
}