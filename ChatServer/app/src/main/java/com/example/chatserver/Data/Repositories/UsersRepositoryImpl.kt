package com.example.chatserver.Data.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Data.db.AppDatabase
import com.example.chatserver.Domain.Repositories.UsersRepository
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
    override fun register(user: User): Single<ResultModel> {
//        var o = Observable.fromCallable { database.usersDao().insertOrUpdateUser(user) }
////            .subscribe{ResultModel(true, "OK!")}
////        return Single.fromObservable{o}
        database.usersDao().insertOrUpdateUser(user)
        return Single.just(ResultModel(true, "OK!"))
    }
}