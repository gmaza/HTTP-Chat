package com.example.chatserver.Data.daos

import androidx.room.*
import com.example.chatserver.Domain.Entities.Chat.ChatWithFriend
import com.example.chatserver.Domain.User.User

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: String): User

    @Query("SELECT * FROM users WHERE name like :name LIMIT :take OFFSET :skip")
    fun getUsers(name: String, skip: Int, take: Int): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUser(user: User): Long

    @Query("SELECT EXISTS(SELECT * FROM users WHERE id = :id)")
    fun isUserIsExist(id : String) : Boolean

    @Query("SELECT COUNT(id) FROM users where name like :filter")
    fun count(filter: String) : Int
}