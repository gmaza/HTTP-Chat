package com.example.chatserver.Domain.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.UseCases.GetUsersResponse
import com.example.chatserver.Domain.User.User
import io.reactivex.Single

interface UsersRepository {

    fun insertOrUpdate(user: User): Single<ResultModel>

    fun get(id: String): Single<User>

    fun isUserExists(id: String): Single<Boolean>

    fun filter(search: String, skip: Int, take: Int): Single<GetUsersResponse>

    fun count(search: String): Single<Int>
}