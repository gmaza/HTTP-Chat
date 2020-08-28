package com.example.chatserver

import android.app.Application
import android.os.StrictMode
import sun.security.krb5.Realm

class ChatApplication : Application() {
    lateinit var appComponent: AppComponent

    private fun initDagger(app: ChatApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

    override fun onCreate() {
        super.onCreate()
        StrictMode
            .setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
        appComponent = initDagger(this)
    }
}