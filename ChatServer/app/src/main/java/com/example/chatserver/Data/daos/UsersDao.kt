package com.example.chatserver.Data.daos

import androidx.room.*
import com.example.chatserver.Domain.User.User

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(note: User): Long
}