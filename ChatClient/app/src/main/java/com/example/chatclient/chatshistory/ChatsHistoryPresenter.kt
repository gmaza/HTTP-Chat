package com.example.chatclient.chatshistory

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ChatsHistoryPresenter(var view: ChatsHistoryView) {

    val JSON_MEDIA = "application/json; charset=utf-8".toMediaTypeOrNull()

    val client = OkHttpClient.Builder()
        .connectionSpecs(
            Arrays.asList(
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT
            )
        )
        .build()

    lateinit var me:String
    fun init(name: String) {
        me = name
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/history?me="+name)
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
                    var histories = mutableListOf<HistoryModel>()
                    val jUsers = (result["messages"] as JSONArray)
                    for(i in 0 until jUsers.length()){
                        histories.add(HistoryModel(jUsers.getJSONObject(i)["user"].toString(),
                            jUsers.getJSONObject(i)["user"].toString(),
                            jUsers.getJSONObject(i)["date"].toString().toLong()))
                    }
                    view.setAdapter(result["quantity"] as Int, false, mutableListOf<UserModel>(), histories)
                }
            }
        })
    }

    fun search(word: String) {
        if(word.length < 3){
            if(word.length == 0)
                init(me)
            return
        }

        val request = Request.Builder()
            .url("http://10.0.2.2:5000?take=20&search="+ word)
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
                    var users = mutableListOf<UserModel>()
                    val jUsers = (result["users"] as JSONArray)
                    for(i in 0 until jUsers.length()){
                        users.add(UserModel(jUsers.getJSONObject(i)["name"].toString()))
                    }
                    view.setAdapter(result["quantity"] as Int, true, users, mutableListOf<HistoryModel>())
                }
            }
        })
    }
}