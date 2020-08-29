package com.example.chatclient.chatshistory

import android.os.Handler
import android.os.Looper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ChatsHistoryPresenter(var view: ChatsHistoryView) {

    val JSON_MEDIA = "application/json; charset=utf-8".toMediaTypeOrNull()
    var lastMessageDate = ""
    var users = mutableListOf<UserModel>()
    var histories = mutableListOf<HistoryModel>()

    val client = OkHttpClient.Builder()
        .connectionSpecs(
            Arrays.asList(
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT
            )
        )
        .build()

        lateinit var mainHandler: Handler

        private val updateTextTask = object : Runnable {
            override fun run() {
                getHistory(1)
                mainHandler.postDelayed(this, 3000)
            }
        }

    fun initTimer(){
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)
    }

   fun pauseTimer() {
        mainHandler.removeCallbacks(updateTextTask)
    }

    fun resumeTimer() {
        mainHandler.post(updateTextTask)
    }

    fun getHistory(page: Int) {
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/history?skip=" + ((page-1) * 10) +"&take="+(page * 10) + "&me="+me)
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }

            override fun onResponse(call: Call, response: Response) {
                var result = JSONObject(response.body!!.string())
                if(result["quantity"] == 0)
                    view.showEmptyScreen()
                else{
                    if((result["messages"] as JSONArray).getJSONObject(0)["date"].toString() == lastMessageDate)
                        return
                    if(page == 1)
                    histories.clear()
                    lastMessageDate = (result["messages"] as JSONArray).getJSONObject(0)["date"].toString()
                    val jUsers = (result["messages"] as JSONArray)
                    for(i in 0 until jUsers.length()){
                        histories.add(HistoryModel(jUsers.getJSONObject(i)["user"].toString(),
                            jUsers.getJSONObject(i)["lastMessage"].toString(),
                            jUsers.getJSONObject(i)["date"].toString().toLong()))
                    }
                    view.setAdapter(false)
                }
            }
        })
    }

    lateinit var me:String
    fun init(name: String) {
        me = name
        view.initAdapters( users, histories)
        initTimer()
    }

    fun search(word: String, page: Int) {
        if(word.length < 3){
            if(word.length == 0){
                resumeTimer()
                init(me)
            }
            return
        }
        lastMessageDate = ""
        pauseTimer()
        val request = Request.Builder()
            .url("http://10.0.2.2:5000?skip=" + ((page-1) * 10) +"&take="+(page * 10) + "&search="+ word)
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }

            override fun onResponse(call: Call, response: Response) {
                var result = JSONObject(response.body!!.string())
                if(result["quantity"] == 0)
                    view.showEmptyScreen()
                else{
                    val jUsers = (result["users"] as JSONArray)
                    for(i in 0 until jUsers.length()){
                        users.add(UserModel(jUsers.getJSONObject(i)["name"].toString()))
                    }
                    view.setAdapter(true)
                }
            }
        })
    }
}