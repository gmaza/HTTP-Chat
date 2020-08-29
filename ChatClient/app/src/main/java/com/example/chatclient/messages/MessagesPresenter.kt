package com.example.chatclient.messages

import android.os.Handler
import android.os.Looper
import com.example.chatclient.chatshistory.HistoryModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MessagesPresenter(val view: MesagesView) {
    lateinit var me:String
    lateinit var friend:String
    var messages = mutableListOf<MessageModel>()
    val JSON_MEDIA = "application/json; charset=utf-8".toMediaTypeOrNull()
    var lastMessageDate = ""


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
            load()
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

    fun Init(me: String, friend: String) {
        this.me = me
        this.friend = friend
        view.initAdapter(messages)
        initTimer()
    }

    fun loadMore() {

    }

    fun sendMessage(message: String) {
        if(message.isNullOrEmpty() ){
            view.showToast("name and profession is required")
            return
        }
        var json = JSONObject()
        json.put("me",me)
        json.put("friend",friend)
        json.put("message",message)

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/messages")
            .post(json.toString().toRequestBody(JSON_MEDIA))
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object :  Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }

            override fun onResponse(call: Call, response: Response) {
                load()
            }
        })
    }

    fun load() {
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/messages?skip=0&take=10&me="+me + "&friend=" + friend)
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }

            override fun onResponse(call: Call, response: Response) {
                var result = JSONObject(response.body!!.string())
                if(result["quantity"] != 0){
                    if((result["messages"] as JSONArray).getJSONObject(0)["date"].toString() == lastMessageDate)
                        return
                    messages.clear()
                    lastMessageDate =
                        (result["messages"] as JSONArray).getJSONObject(0)["date"].toString()
                    val jUsers = (result["messages"] as JSONArray)
                    for(i in 0 until jUsers.length()){
                        messages.add(
                            MessageModel(jUsers.getJSONObject(i)["message"].toString(),
                                jUsers.getJSONObject(i)["isSender"].toString().toBoolean(),
                                jUsers.getJSONObject(i)["date"].toString().toLong())
                        )
                    }
                    view.notifiAdapter()
                }
            }
        })
    }
}