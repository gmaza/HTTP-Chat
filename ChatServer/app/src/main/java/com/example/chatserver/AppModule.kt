package com.example.chatserver

import android.app.Application
import android.content.Context
import com.example.chatserver.Data.Repositories.UsersRepositoryImpl
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.UseCases.RegisterUserUserCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideRegisterUserUserCase(): RegisterUserUserCase = RegisterUserUserCase(UsersRepositoryImpl(
        AppDatabase.getInstance(provideContext())))
}