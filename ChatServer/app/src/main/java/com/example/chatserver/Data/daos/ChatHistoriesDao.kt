package com.example.chatserver.Data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chatserver.Domain.Entities.Chat.ChatHistory

@Dao
interface ChatHistoriesDao {
    @Query("SELECT * FROM chat_histories WHERE user1_id = :userid LIMIT :take OFFSET :skip")
    fun getHistories(userid: String, skip: Int, take: Int): List<ChatHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateHistorye(history: ChatHistory): Long

    @Query("SELECT COUNT(id) FROM chat_histories where user1_id = :id")
    fun count(id: String) : Int
}