package com.example.chatclient.login

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import kotlin.math.log


class LoginPresenter(var loginView: LoginView) {
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

    fun checkConnection() {
        val request = Request.Builder()
            .url("http://10.0.2.2:5000")
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object :  Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }

            override fun onResponse(call: Call, response: Response) {
                loginView.showLoginForm()
            }
        })
    }

    fun register(name: String, profession: String, icon: String){
        if(name.isNullOrEmpty() || profession.isNullOrEmpty()){
            loginView.showToast("name and profession is required")
            return
        }
        var json = JSONObject()
        json.put("name",name)
        json.put("profession",profession).toString()
        if(!icon.isNullOrEmpty())
        json.put("icon",icon).toString()

        val request = Request.Builder()
            .url("http://10.0.2.2:5000/register")
            .post(json.toString().toRequestBody(JSON_MEDIA))
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object :  Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("error")
            }

            override fun onResponse(call: Call, response: Response) {
                var result = response.body!!.string()
                if(result == "OK")
                    loginView.openHistoryView(name)
                else {
                    loginView.showToast(result)
                    loginView.openHistoryView(name)
                }
            }
        })
    }
}
