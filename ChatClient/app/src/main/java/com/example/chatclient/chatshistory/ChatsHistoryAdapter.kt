package com.example.chatclient.chatshistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatclient.R
import kotlinx.android.synthetic.main.chat_history_item.view.*

class ChatsHistoryAdapter(val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_history_item , parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.friendName?.text = items.get(position)
    }


}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
   val friendName = view.tv_chat_name
}