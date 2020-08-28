package com.example.chatserver.Domain.UseCases

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.User.User
import io.reactivex.Single
import javax.inject.Inject

class RegisterUserUserCase @Inject constructor(private val usersRepo: UsersRepository) :
    SingleUseCase<ResultModel, RegisterUserUserCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ResultModel> {
        return with(params) {
            usersRepo.register(User("Asd","asd", "asd"))
        }
    }

    data class Params(
        val name: String,
        val profession: String
    )
}