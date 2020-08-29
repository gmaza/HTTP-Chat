package com.example.chatserver.Data.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.Repositories.MessagesReposiory
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.UseCases.GetUsersResponse
import io.reactivex.Single
import javax.inject.Inject

class MessagesReposioryImpl  @Inject constructor(
    private val database: AppDatabase
) :
    MessagesReposiory {
    override fun insertOrUpdate(message: Message): ResultModel {
        database.messagesDao().insertOrUpdateMessage(message)
        return ResultModel(true, "OK")
    }

    override fun get(chatHistoryID: String, skip: Int, take: Int): List<Message> {
        return database.messagesDao().getMessages(chatHistoryID, skip, take)
    }

    override fun count(chatHistoryID: String): Int {
        return  database.messagesDao().count(chatHistoryID)
    }

    override fun delete(historyID: String): ResultModel {
         database.messagesDao().delete(historyID)
        return ResultModel(true, "OK")
    }
}