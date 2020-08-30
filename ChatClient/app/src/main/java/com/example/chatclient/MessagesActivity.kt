package com.example.chatclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatclient.chatshistory.ChatsHistoryAdapter
import com.example.chatclient.chatshistory.ChatsHistoryPresenter
import com.example.chatclient.chatshistory.ChatsHistoryUsersAdapter
import com.example.chatclient.messages.MesagesView
import com.example.chatclient.messages.MessageModel
import com.example.chatclient.messages.MessagesAdapter
import com.example.chatclient.messages.MessagesPresenter
import com.example.chatclient.utils.SoftInputAssist
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity(), MesagesView {

    private val presenter = MessagesPresenter(this)
    private lateinit var softInputAssist: SoftInputAssist

    private  lateinit var messagesAdapter: MessagesAdapter
    private var linear = LinearLayoutManager(this, RecyclerView.VERTICAL,true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_messages)


        toolbarh.setNavigationIcon(R.drawable.back)
        //setSupportActionBar(toolbarh)

        //toolbar_layout.setExpandedTitleTextAppearance()

        softInputAssist = SoftInputAssist(this)
        val friend = intent.getStringExtra("friend")
        val me = intent.getStringExtra("me")
        presenter.Init(me, friend)

        txt_message.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                presenter.sendMessage(txt_message.text.toString())
                txt_message.setText("")
                return@OnKeyListener true
            }
            false
        })
    }

    override fun initAdapter(messages: MutableList<MessageModel>) {
        messagesAdapter = MessagesAdapter(messages, this)
        rv_messages.layoutManager = linear
        rv_messages.adapter = messagesAdapter
    }

    override fun notifiAdapter() {
        runOnUiThread {
            messagesAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        softInputAssist.onResume()
        super.onResume()
    }

    override fun onPause() {
        softInputAssist.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        softInputAssist.onDestroy()
        super.onDestroy()
    }


    override fun showToast(message: String) {
        runOnUiThread {
            val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            toast.show()
        }
    }
}
