package com.example.chatserver.Data.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.ChatWithFriend
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.Repositories.ChatHitoriesRepository
import com.example.chatserver.Domain.Repositories.UsersRepository
import javax.inject.Inject

class ChatHitoriesRepositoryImlp  @Inject constructor(
    private val database: AppDatabase
) :
    ChatHitoriesRepository {
    override fun insertOrUpdate(history: ChatHistory): ResultModel {
        database.chatHistoriesDao().insertOrUpdateHistorye(history)
        return ResultModel(true, "OK")
    }

    override fun get(userid: String, skip: Int, take: Int): List<ChatWithFriend> {
        return  database.chatHistoriesDao().getHistoriesWithFriends(userid, skip, take)
    }

    override fun count(chatHistoryID: String): Int {
        return  database.chatHistoriesDao().count(chatHistoryID)
    }

    override fun delete(id: String): ResultModel {
        database.chatHistoriesDao().delete(id)
        return ResultModel(true, "OK")
    }
}