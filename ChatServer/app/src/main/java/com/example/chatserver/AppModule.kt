package com.example.chatserver

import android.app.Application
import android.content.Context
import com.example.chatserver.Data.Repositories.UsersRepositoryImpl
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.UseCases.GetUserUseCase
import com.example.chatserver.Domain.UseCases.GetUsersUseCase
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
    fun provideDatabase(): AppDatabase =  AppDatabase.getInstance(provideContext())

    @Provides
    @Singleton
    fun provideUsersRepository(): UsersRepository = UsersRepositoryImpl(provideDatabase())

    @Provides
    @Singleton
    fun provideRegisterUserUserCase(): RegisterUserUserCase = RegisterUserUserCase(provideUsersRepository())

    @Provides
    @Singleton
    fun provideGetUserUserCase(): GetUserUseCase = GetUserUseCase(provideUsersRepository())

    @Provides
    @Singleton
    fun provideGetUsersUserCase(): GetUsersUseCase = GetUsersUseCase(provideUsersRepository())
}