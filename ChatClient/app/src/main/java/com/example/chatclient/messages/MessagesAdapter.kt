package com.example.chatclient.messages

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassifier.TYPE_EMAIL
import androidx.recyclerview.widget.RecyclerView
import com.example.chatclient.R
import com.example.chatclient.chatshistory.ChatsHistoryUsers
import com.example.chatclient.utils.lastMessageDate
import kotlinx.android.synthetic.main.chat_history_item.view.*
import kotlinx.android.synthetic.main.message_from_me_item.view.*
import kotlinx.android.synthetic.main.message_to_me_item.view.*


class MessagesAdapter( var messages : MutableList<MessageModel>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (messages.get(position).isSender) {
            1
        } else {
            2
        }
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        if(viewType == 1)
            return messagesFromHolder(LayoutInflater.from(context).inflate(R.layout.message_from_me_item , parent, false))
        return messagesToolder(LayoutInflater.from(context).inflate(R.layout.message_to_me_item , parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var message = messages[position]
        if(message.isSender){
            var h = holder as messagesFromHolder
            h?.message?.text = message.message
            h?.date?.text = message.date.lastMessageDate()
        } else {
            var h = holder as messagesToolder
            h?.message?.text = message.message
            h?.date?.text = message.date.lastMessageDate()
        }
    }

    class messagesFromHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val message = view.tv_message_from_me
        val date = view.tv_last_msg_time_from
    }

    class messagesToolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val message = view.tv_message_to_me
        val date = view.tv_last_msg_time_to
    }
}


