package com.example.chatserver

import com.example.chatserver.Api.ChatApiService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: ChatApiService)
}