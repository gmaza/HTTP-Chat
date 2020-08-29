package com.example.chatserver

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.chatserver.Api.ChatApiService
import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.UseCases.Chats.*
import com.example.chatserver.Domain.UseCases.GetUserUseCase
import com.example.chatserver.Domain.UseCases.GetUsersUseCase
import com.example.chatserver.Domain.UseCases.RegisterUserUserCase
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceClass = ChatApiService::class.java
        val intent = Intent(applicationContext, serviceClass)

        if (isServiceRunning(serviceClass)) {
            serverTextView.text = getString(R.string.server_running)
            serverButton.text = getString(R.string.stop_server)
        }

        serverButton.setOnClickListener {
            if (!isServiceRunning(serviceClass)) {
                startService(intent)
                serverTextView.text = getString(R.string.server_running)
                serverButton.text = getString(R.string.stop_server)
            } else {
                stopService(intent)
                serverTextView.text = getString(R.string.server_down)
                serverButton.text = getString(R.string.start_server)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
}