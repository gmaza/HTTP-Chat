package com.example.chatserver.Domain.Entities.Chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_histories",
    indices = [Index("id", unique = true)]
)
data class ChatHistory(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "user1_id")
    var user1: String,

    @ColumnInfo(name = "user2_id")
    var user2: String,

    @ColumnInfo(name = "last_message")
    var lastMessage: String,

    @ColumnInfo(name = "update_date")
    var updateDate: Long
)

