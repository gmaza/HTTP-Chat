package com.example.chatserver.Domain.UseCases.Chats

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.Repositories.ChatHitoriesRepository
import com.example.chatserver.Domain.Repositories.MessagesReposiory
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.UseCases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class RemoveHistoryUseCase @Inject constructor(private val historyRepository: ChatHitoriesRepository,
                                             private val messagesReposiory: MessagesReposiory) :
    SingleUseCase<ResultModel, RemoveHistoryUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ResultModel> {
        return with(params) {
            historyRepository.delete(params.me + params.friend)
            messagesReposiory.delete(params.me + params.friend)
            return Single.just(ResultModel(true,"OK"))
        }
    }

    data class Params(
        val me: String,
        val friend: String
    )
}