package com.example.chatserver.Data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chatserver.Data.daos.ChatHistoriesDao
import com.example.chatserver.Data.daos.MessagesDao
import com.example.chatserver.Data.daos.UsersDao
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.User.User


private const val DATABASE = "notes"

@Database(
    entities = [User::class, ChatHistory::class, Message::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun messagesDao(): MessagesDao
    abstract fun chatHistoriesDao(): ChatHistoriesDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE)
                .build()
        }
    }
}