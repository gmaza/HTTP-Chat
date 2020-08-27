package com.example.chatserver.Domain.Repositories

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.User.User
import io.reactivex.Single

interface UsersRepository {

    fun register(user: User): Single<ResultModel>
}