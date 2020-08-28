package com.example.chatserver.Domain.UseCases

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.User.User
import io.reactivex.Single
import javax.inject.Inject

data class GetUsersResponse(val users: List<User>, val quantity: Int)
class GetUsersUseCase @Inject constructor(private val usersRepo: UsersRepository) :
    SingleUseCase<GetUsersResponse, GetUsersUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<GetUsersResponse> {
        return with(params) {
            usersRepo.filter(params.search, skip, take)
        }
    }

    data class Params(
        val search: String,
        val skip: Int,
        val take: Int
    )
}