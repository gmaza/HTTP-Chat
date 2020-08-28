package com.example.chatserver.Domain.UseCases.Chats

import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.Repositories.MessagesReposiory
import com.example.chatserver.Domain.UseCases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

data class GetMessagesResponse(val messages: List<Message>, val quantity :Int)
class GetMessagesWIthFriendUseCase @Inject constructor(private val messagesReposiory: MessagesReposiory) :
    SingleUseCase<GetMessagesResponse, GetMessagesWIthFriendUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<GetMessagesResponse> {
        return with(params) {
            var result = GetMessagesResponse(messagesReposiory.get(params.me + params.friend,
                params.skip, params.take), messagesReposiory.count(params.me + params.friend))

            return Single.just(result)
        }
    }

    data class Params(
        val me: String,
        val friend: String,
        val skip: Int,
        val take: Int
    )
}