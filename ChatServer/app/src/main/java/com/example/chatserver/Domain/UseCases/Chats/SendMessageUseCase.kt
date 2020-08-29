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

class SendMessageUseCase @Inject constructor(private val historyRepository: ChatHitoriesRepository,
                                             private val messagesReposiory: MessagesReposiory,
                                             private val usersRepository: UsersRepository) :
    SingleUseCase<ResultModel, SendMessageUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ResultModel> {
        return with(params) {

            if(params.senderName == params.receiverName)
                return Single.just(ResultModel(false,"Can't message to yourself"))

            if(!usersRepository.isUserExists(params.senderName).blockingGet())
                return Single.just(ResultModel(false,"sender user doesn't exists"))

            if(!usersRepository.isUserExists(params.receiverName).blockingGet())
                return Single.just(ResultModel(false,"receiverName user doesn't exists"))

            if(params.message.isEmpty())
                return Single.just(ResultModel(false,"message is empty"))

            // Insert or update chat histories for both users to track last chating date.

            historyRepository.insertOrUpdate(ChatHistory(params.senderName + params.receiverName,
                params.senderName, params.receiverName, params.message, System.currentTimeMillis()))

            messagesReposiory.insertOrUpdate(Message(System.currentTimeMillis(),
                params.senderName + params.receiverName, params.message, true, System.currentTimeMillis()))

            historyRepository.insertOrUpdate(ChatHistory(params.receiverName + params.senderName,
                params.receiverName, params.senderName, params.message, System.currentTimeMillis()))

            messagesReposiory.insertOrUpdate(Message(System.currentTimeMillis(),
                params.receiverName + params.senderName, params.message, false, System.currentTimeMillis()))

            return Single.just(ResultModel(true,"OK"))
        }
    }

    data class Params(
        val senderName: String,
        val receiverName: String,
        val message: String
    )
}