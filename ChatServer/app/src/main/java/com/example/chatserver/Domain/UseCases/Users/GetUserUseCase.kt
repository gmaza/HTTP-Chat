package com.example.chatserver.Domain.UseCases

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.User.User
import io.reactivex.Single
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val usersRepo: UsersRepository) :
    SingleUseCase<User, GetUserUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<User> {
        return with(params) {
            usersRepo.get(params.id)
        }
    }

    data class Params(
        val id: String
    )
}