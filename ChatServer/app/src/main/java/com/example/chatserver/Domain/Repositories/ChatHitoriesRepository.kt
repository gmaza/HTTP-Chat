package com.example.chatserver.Domain.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.ChatWithFriend
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.UseCases.GetUsersResponse
import com.example.chatserver.Domain.User.User
import io.reactivex.Single

interface ChatHitoriesRepository {
    fun insertOrUpdate(chatHistory: ChatHistory): ResultModel

    fun get(chatid: String, skip: Int, take: Int): List<ChatWithFriend>

    fun count(search: String): Int

    fun delete(id: String): ResultModel
}