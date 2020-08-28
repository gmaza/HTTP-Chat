package com.example.chatserver.Data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chatserver.Domain.Entities.Chat.Message

@Dao
interface MessagesDao {

    @Query("SELECT * FROM messages WHERE history_id = :chatHistoryID LIMIT :take OFFSET :skip")
    fun getMessages(chatHistoryID: String, skip: Int, take: Int): List<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateMessage(message: Message): Long

    @Query("SELECT COUNT(id) FROM messages where history_id =  :chatHistoryID")
    fun count(chatHistoryID: String) : Int
}