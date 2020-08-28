package com.example.chatserver.Domain.Entities.Chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index("id", unique = true)]
)
data class Message(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "history_id")
    var ChatHistory: String,

    @ColumnInfo(name = "value")
    var value: String,

    @ColumnInfo(name = "is_sender")
    var isSender: Boolean,

    @ColumnInfo(name = "update_date")
    var updateDate: Long
){

}

