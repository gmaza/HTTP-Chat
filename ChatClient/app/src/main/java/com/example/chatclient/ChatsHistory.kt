package com.example.chatclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatclient.chatshistory.*
import com.example.chatclient.login.LoginPresenter
import com.example.chatclient.utils.EndlessRecyclerViewScrollListener
import com.example.chatclient.utils.SoftInputAssist
import kotlinx.android.synthetic.main.activity_chats_history.*

class ChatsHistory : AppCompatActivity(), ChatsHistoryView {
    private val presenter = ChatsHistoryPresenter(this)
    private lateinit var softInputAssist: SoftInputAssist
    private  lateinit var usersAdapter: ChatsHistoryUsersAdapter
    private  lateinit var chatsHistoryAdapter: ChatsHistoryAdapter
    private var linear = LinearLayoutManager(this)
    private  var searchM = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_chats_history)
        softInputAssist = SoftInputAssist(this)
        val me = intent.getStringExtra("name")
        rv_chats_history.layoutManager = linear


        txt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.search(s.toString(), 1)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        presenter.init(me)
        rv_chats_history.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linear){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
               if(searchM)
                   presenter.search(txt_search.text.toString(), page)
                else
                   presenter.getHistory(page)
            }
        })
    }

    override fun setAdapter(searchMode: Boolean) {
        searchM = searchMode
        runOnUiThread { no_chat_history_msg.visibility = View.GONE
            rv_chats_history.visibility = View.VISIBLE
            if (searchMode) {
                if(rv_chats_history.adapter != usersAdapter)
                    rv_chats_history.adapter = usersAdapter
                usersAdapter.notifyDataSetChanged();
            } else {
                if(rv_chats_history.adapter != chatsHistoryAdapter)
                    rv_chats_history.adapter = chatsHistoryAdapter
                chatsHistoryAdapter.notifyDataSetChanged();
            }
        }
    }

    override fun showEmptyScreen() {
        runOnUiThread {
            no_chat_history_msg.visibility = View.VISIBLE
            rv_chats_history.visibility = View.GONE
        }
    }

    override fun initAdapters(users: MutableList<UserModel>, histories: MutableList<HistoryModel>) {
        usersAdapter = ChatsHistoryUsersAdapter(users.size, users, this)
        chatsHistoryAdapter = ChatsHistoryAdapter(histories.size, histories, this)
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
}