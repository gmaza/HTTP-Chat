package com.example.chatserver.Api

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.example.chatserver.ChatApplication
import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.UseCases.Chats.*
import com.example.chatserver.Domain.UseCases.GetUserUseCase
import com.example.chatserver.Domain.UseCases.GetUsersUseCase
import com.example.chatserver.Domain.UseCases.RegisterUserUserCase
import com.example.chatserver.R
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class ChatApiService : Service() {

    private var serverUp = false

    @Inject
    lateinit var registerUserUseCase : RegisterUserUserCase

    @Inject
    lateinit var getUserUseCase : GetUserUseCase

    @Inject
    lateinit var getUsersUseCase : GetUsersUseCase

    @Inject
    lateinit var sendMessageUseCase : SendMessageUseCase

    @Inject
    lateinit var getMessagesWIthFriendUseCase : GetMessagesWIthFriendUseCase

    @Inject
    lateinit var getHistoriesUseCase : GetHistoriesUseCase

    @Inject
    lateinit var removeHistoryUseCase : RemoveHistoryUseCase

    var result : ResultModel?  = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var a = (application as ChatApplication)
        a.appComponent.inject(this)

        var port = 5000
        serverUp = if(!serverUp){
            startServer(port)
            true
        } else{
            stopServer()
            false
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        stopServer()
        super.onDestroy()
    }


    private fun streamToString(inputStream: InputStream): String {
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    private fun sendResponse(httpExchange: HttpExchange, responseText: String){
        httpExchange.sendResponseHeaders(200, responseText.length.toLong())
        val os = httpExchange.responseBody
        os.write(responseText.toByteArray())
        os.close()
    }

    private var mHttpServer: HttpServer? = null


    private fun startServer(port: Int) {
        try {
            mHttpServer = HttpServer.create(InetSocketAddress(port), 0)
            mHttpServer!!.executor = Executors.newCachedThreadPool()

            mHttpServer!!.createContext("/", rootHandler)
            mHttpServer!!.createContext("/index", rootHandler)
            // Handle /messages endpoint
            mHttpServer!!.createContext("/messages", messageHandler)
            // Handle /messages endpoint
            mHttpServer!!.createContext("/history", historyHandler)
            // Handle /register endpoint
            mHttpServer!!.createContext("/register", registrationHandler)
            mHttpServer!!.start()//startServer server;

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopServer() {
        if (mHttpServer != null){
            mHttpServer!!.stop(0)
        }
    }

    fun getQueryMap(query: String): Map<String, String>? {
        val params = query.split("&").toTypedArray()
        val map: MutableMap<String, String> =
            HashMap()
        for (param in params) {
            val name = param.split("=").toTypedArray()[0]
            val value = param.split("=").toTypedArray()[1]
            map[name] = value
        }
        return map
    }

    // Handler for root endpoint
    private val rootHandler = HttpHandler { exchange ->
        run {
            // Get request method
            when (exchange!!.requestMethod) {
                "GET" -> {
                    var search: String = ""
                    var skip: Int = 0
                    var take: Int = 10
                    if(exchange.requestURI.query?.isNullOrEmpty() == false) {
                        var q = getQueryMap(exchange.requestURI.query)
                        search = q!!["search"] ?: ""
                        skip = q!!["skip"]?.toInt() ?: 0
                        take = q!!["take"]?.toInt() ?: 10
                    }
                    getUsersUseCase.execute(
                        onSuccess = { r -> sendResponse(exchange, r.ToJson()) },
                        onError = { result = ResultModel(false, "unknown error") },
                        params = GetUsersUseCase.Params(search, skip, take)
                    )
                }
            }
        }

    }

    //    @Serializable
    data class Data(val a: Int, val str: String = "str")

    private val messageHandler = HttpHandler { httpExchange ->
        run {
            when (httpExchange!!.requestMethod) {
                "GET" -> {
                    var me: String = ""
                    var friend: String = ""
                    var skip: Int = 0
                    var take: Int = 10
                    if(httpExchange.requestURI.query?.isNullOrEmpty() == false) {
                        var q = getQueryMap(httpExchange.requestURI.query)
                        me = q!!["me"] ?: ""
                        friend = q!!["friend"] ?: ""
                        skip = q!!["skip"]?.toInt() ?: 0
                        take = q!!["take"]?.toInt() ?: 10
                    }
                    getMessagesWIthFriendUseCase.execute(
                        onSuccess = { r: GetMessagesResponse ->sendResponse(httpExchange, r.ToJson()) },
                        onError = { result = ResultModel(false, "uknown problem") },
                        params = GetMessagesWIthFriendUseCase.Params(me, friend, skip, take)
                    )
                }
                "POST" -> {
                    val inputStream = httpExchange.requestBody

                    val requestBody = streamToString(inputStream)
                    val jsonBody = JSONObject(requestBody)

                    sendMessageUseCase.execute(
                        onSuccess = { r: ResultModel ->sendResponse(httpExchange, r.message) },
                        onError = { result = ResultModel(false, "uknown problem") },
                        params = SendMessageUseCase.Params(jsonBody["me"].toString(),
                                                        jsonBody["friend"].toString(),
                                                        jsonBody["message"].toString())
                    )
                }

            }
        }
    }

    private val historyHandler = HttpHandler { httpExchange ->
        run {
            when (httpExchange!!.requestMethod) {
                "GET" -> {
                    var me: String = ""
                    var skip: Int = 0
                    var take: Int = 10
                    if(httpExchange.requestURI.query?.isNullOrEmpty() == false) {
                        var q = getQueryMap(httpExchange.requestURI.query)
                        me = q!!["me"] ?: ""
                        skip = q!!["skip"]?.toInt() ?: 0
                        take = q!!["take"]?.toInt() ?: 10
                    }
                    getHistoriesUseCase.execute(
                        onSuccess = { r ->sendResponse(httpExchange, r.ToJson()) },
                        onError = { result = ResultModel(false, "uknown problem") },
                        params = GetHistoriesUseCase.Params(me, skip, take)
                    )
                }
                "DELETE" -> {
                    val inputStream = httpExchange.requestBody

                    val requestBody = streamToString(inputStream)
                    val jsonBody = JSONObject(requestBody)

                    removeHistoryUseCase.execute(
                        onSuccess = { r: ResultModel ->sendResponse(httpExchange, r.message) },
                        onError = { result = ResultModel(false, "uknown problem") },
                        params = RemoveHistoryUseCase.Params(jsonBody["me"].toString(),
                                                                jsonBody["friend"].toString())
                    )
                }
            }
        }
    }

    private val registrationHandler = HttpHandler { httpExchange ->
        run {
            when (httpExchange!!.requestMethod) {
                "POST" -> {
                    val inputStream = httpExchange.requestBody

                    val requestBody = streamToString(inputStream)
                    val jsonBody = JSONObject(requestBody)

                    var icon = if (jsonBody.has("icon")) jsonBody["icon"].toString() else ""

                    registerUserUseCase.execute(
                        onSuccess = { r: ResultModel ->sendResponse(httpExchange, r.message) },
                        onError = { result = ResultModel(false, "uknown problem") },
                        params = RegisterUserUserCase.Params(jsonBody["name"].toString(),
                                                            jsonBody["profession"].toString(),
                                                            icon)
                    )
                }

            }
        }
    }

}