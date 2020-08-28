package com.example.chatserver

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.UseCases.Chats.GetMessagesResponse
import com.example.chatserver.Domain.UseCases.Chats.GetMessagesWIthFriendUseCase
import com.example.chatserver.Domain.UseCases.Chats.SendMessageUseCase
import com.example.chatserver.Domain.UseCases.GetUserUseCase
import com.example.chatserver.Domain.UseCases.GetUsersUseCase
import com.example.chatserver.Domain.UseCases.RegisterUserUserCase
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


class MainActivity : AppCompatActivity() {

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


    var result : ResultModel?  = null

    fun test(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var a = (application as ChatApplication)
        a.appComponent.inject(this)
        test()
       // setSupportActionBar(toolbar)
        val port = 5000

        serverButton.setOnClickListener {
            serverUp = if(!serverUp){
                startServer(port)
                true
            } else{
                stopServer()
                false
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
            // Handle /register endpoint
            mHttpServer!!.createContext("/register", registrationHandler)
            mHttpServer!!.start()//startServer server;
            serverTextView.text = getString(R.string.server_running)
            serverButton.text = getString(R.string.stop_server)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun stopServer() {
        if (mHttpServer != null){
            mHttpServer!!.stop(0)
            serverTextView.text = getString(R.string.server_down)
            serverButton.text = getString(R.string.start_server)
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
                        onSuccess = { r -> sendResponse(exchange, r.quantity.toString()) },
                        onError = { result = ResultModel(false, "dont know") },
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
                        onSuccess = { r: GetMessagesResponse ->sendResponse(httpExchange, r.toString()) },
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
                        params = SendMessageUseCase.Params(jsonBody["me"].toString(), jsonBody["friend"].toString(), jsonBody["message"].toString())
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

                    registerUserUseCase.execute(
                        onSuccess = { r: ResultModel ->sendResponse(httpExchange, r.message) },
                        onError = { result = ResultModel(false, "uknown problem") },
                        params = RegisterUserUserCase.Params(jsonBody["name"].toString(), jsonBody["profession"].toString())
                    )
                }

            }
        }
    }
}