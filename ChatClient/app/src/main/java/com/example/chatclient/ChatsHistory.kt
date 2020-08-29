package com.example.chatclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatclient.chatshistory.*
import com.example.chatclient.login.LoginPresenter
import kotlinx.android.synthetic.main.activity_chats_history.*

class ChatsHistory : AppCompatActivity(), ChatsHistoryView {
    private val presenter = ChatsHistoryPresenter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_chats_history)
        val me = intent.getStringExtra("name")

        txt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        presenter.init(me)
    }

    val animals: ArrayList<String> = ArrayList()

    override fun setAdapter(quantity: Int, searchMode: Boolean, users: MutableList<UserModel>,  histories: MutableList<HistoryModel>) {
        runOnUiThread {
            no_chat_history_msg.visibility = View.GONE
            rv_chats_history.visibility = View.VISIBLE
            if (searchMode) {
                rv_chats_history.layoutManager = LinearLayoutManager(this)
                rv_chats_history.adapter = ChatsHistoryUsersAdapter(quantity, users, this)
            } else {
                rv_chats_history.layoutManager = LinearLayoutManager(this)
                rv_chats_history.adapter = ChatsHistoryAdapter(quantity, histories, this)
            }
        }
    }

    override fun showEmptyScreen() {
        no_chat_history_msg.visibility = View.VISIBLE
        rv_chats_history.visibility = View.GONE
    }
}
