package com.example.chatserver.Data.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.UseCases.GetUsersResponse
import com.example.chatserver.Domain.User.User
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) :
    UsersRepository {
    override fun insertOrUpdate(user: User): Single<ResultModel> {
        database.usersDao().insertOrUpdateUser(user)
        return Single.just(ResultModel(true, "OK"))
    }

    override fun isUserExists(id: String): Single<Boolean> {
        var exists = database.usersDao().isUserIsExist(id)
        return Single.just(exists)
    }

    override fun get(id: String): Single<User> {
        var user = database.usersDao().getUser(id)
        return Single.just(user)
    }

    override fun filter(search: String, skip: Int, take: Int): Single<GetUsersResponse> {
        var s = "%" + search + "%"
        var users = database.usersDao().getUsers(s, skip, take)
        var quantity = database.usersDao().count(s)
        return Single.just(GetUsersResponse(users, quantity))
    }

    override fun count(search: String): Single<Int> {
        var quantity = database.usersDao().count(search)
        return Single.just(quantity)
    }
}