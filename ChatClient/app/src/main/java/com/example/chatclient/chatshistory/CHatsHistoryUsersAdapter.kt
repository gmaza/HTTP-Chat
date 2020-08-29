package com.example.chatclient.chatshistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatclient.R
import kotlinx.android.synthetic.main.chat_history_item.view.*


class ChatsHistoryUsersAdapter(val quantity: Int, var users : MutableList<UserModel>, val context: Context) : RecyclerView.Adapter<ChatsHistoryUsers>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsHistoryUsers {
        return ChatsHistoryUsers(LayoutInflater.from(context).inflate(R.layout.chat_history_item , parent, false))
    }

    override fun onBindViewHolder(holder: ChatsHistoryUsers, position: Int) {
        holder?.friendName?.text = users.get(position).name
        holder?.lastMessage?.text = users.get(position).name
    }
}

class ChatsHistoryUsers (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val friendName = view.tv_chat_name
    val lastMessage = view.tv_last_message
    val lastMessageTime = view.tv_last_message_time
}