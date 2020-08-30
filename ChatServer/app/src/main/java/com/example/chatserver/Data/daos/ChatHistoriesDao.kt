package com.example.chatserver.Data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.ChatWithFriend

@Dao
interface ChatHistoriesDao {
    @Query("SELECT * FROM chat_histories WHERE user1_id = :userid ORDER BY update_date DESC LIMIT :take OFFSET :skip")
    fun getHistories(userid: String, skip: Int, take: Int): List<ChatHistory>

    @Query("SELECT * FROM chat_histories WHERE user1_id = :userid ORDER BY update_date DESC LIMIT :take OFFSET :skip")
    fun getHistoriesWithFriends(userid: String, skip: Int, take: Int): List<ChatWithFriend>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateHistorye(history: ChatHistory): Long

    @Query("SELECT COUNT(id) FROM chat_histories where user1_id = :id")
    fun count(id: String) : Int

    @Query("DELETE FROM chat_histories where id = :id")
    fun delete(id: String)
}