package com.example.chatserver.Domain.Entities.Chat

import androidx.room.Embedded
import androidx.room.Relation
import com.example.chatserver.Domain.User.User

data class ChatWithFriend (
    @Embedded
    val chatHistory: ChatHistory,
    @Relation(
        parentColumn = "user2_id",
        entityColumn = "id"
    )
    val user: User
)