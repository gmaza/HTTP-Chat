package com.example.chatserver.Domain.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.UseCases.GetUsersResponse
import io.reactivex.Single

interface MessagesReposiory {
    fun insertOrUpdate(message: Message): ResultModel

    fun get(chatHistoryID: String, skip: Int, take: Int): List<Message>

    fun count(search: String): Int

    fun delete(historyID: String): ResultModel
}