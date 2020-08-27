package com.example.chatserver.Data.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.User.User
import io.reactivex.Single
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) :
    UsersRepository {
    override fun register(user: User): Single<ResultModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}