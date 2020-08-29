package com.example.chatclient.chatshistory

interface ChatsHistoryView {
    fun setAdapter(searchMode: Boolean)

    fun showEmptyScreen()

    fun initAdapters(users: MutableList<UserModel>, histories: MutableList<HistoryModel>)
}