package com.example.chatserver.Data.daos

import androidx.room.*
import com.example.chatserver.Domain.User.User

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(note: User): Long
}