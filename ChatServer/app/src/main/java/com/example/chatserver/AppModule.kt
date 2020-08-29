package com.example.chatserver

import android.app.Application
import android.content.Context
import com.example.chatserver.Data.Repositories.ChatHitoriesRepositoryImlp
import com.example.chatserver.Data.Repositories.MessagesReposioryImpl
import com.example.chatserver.Data.Repositories.UsersRepositoryImpl
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Repositories.ChatHitoriesRepository
import com.example.chatserver.Domain.Repositories.MessagesReposiory
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.UseCases.Chats.GetHistoriesUseCase
import com.example.chatserver.Domain.UseCases.Chats.GetMessagesWIthFriendUseCase
import com.example.chatserver.Domain.UseCases.Chats.RemoveHistoryUseCase
import com.example.chatserver.Domain.UseCases.Chats.SendMessageUseCase
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
    fun provideMessagesRepository(): MessagesReposiory = MessagesReposioryImpl(provideDatabase())

    @Provides
    @Singleton
    fun providHistoriesRepository(): ChatHitoriesRepository = ChatHitoriesRepositoryImlp(provideDatabase())

    @Provides
    @Singleton
    fun provideRegisterUserUserCase(): RegisterUserUserCase = RegisterUserUserCase(provideUsersRepository())

    @Provides
    @Singleton
    fun provideGetUserUserCase(): GetUserUseCase = GetUserUseCase(provideUsersRepository())

    @Provides
    @Singleton
    fun provideGetUsersUserCase(): GetUsersUseCase = GetUsersUseCase(provideUsersRepository())

    @Provides
    @Singleton
    fun provideGetMessagesWIthFriendUseCase(): GetMessagesWIthFriendUseCase = GetMessagesWIthFriendUseCase(provideMessagesRepository())

    @Provides
    @Singleton
    fun provideGetHistoriesUseCase(): GetHistoriesUseCase = GetHistoriesUseCase(providHistoriesRepository())

    @Provides
    @Singleton
    fun provideRemoveHistoryUseCase(): RemoveHistoryUseCase = RemoveHistoryUseCase(providHistoriesRepository(), provideMessagesRepository())

    @Provides
    @Singleton
    fun provideSendMessageUserCase(): SendMessageUseCase = SendMessageUseCase(providHistoriesRepository(), provideMessagesRepository() ,provideUsersRepository())
}