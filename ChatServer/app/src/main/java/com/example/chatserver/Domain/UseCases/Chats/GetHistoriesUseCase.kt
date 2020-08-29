package com.example.chatserver.Domain.UseCases.Chats

import com.example.chatserver.Domain.Entities.Chat.ChatHistory
import com.example.chatserver.Domain.Entities.Chat.ChatWithFriend
import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.Repositories.ChatHitoriesRepository
import com.example.chatserver.Domain.Repositories.MessagesReposiory
import com.example.chatserver.Domain.UseCases.SingleUseCase
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class GetHistoriesUseCase @Inject constructor(private val hitoriesRepository: ChatHitoriesRepository) :
    SingleUseCase<GetHistoriesResponse, GetHistoriesUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<GetHistoriesResponse> {
        return with(params) {
            var result = GetHistoriesResponse(hitoriesRepository.get(params.me,
                params.skip, params.take), hitoriesRepository.count(params.me))

            return Single.just(result)
        }
    }

    data class Params(
        val me: String,
        val skip: Int,
        val take: Int
    )
}


data class GetHistoriesResponse(val messages: List<ChatWithFriend>, val quantity :Int){
    fun ToJson() : String {
        var res = JSONObject()

        res.put("quantity", quantity)

        var historiesJson = JSONArray()
        for(it in messages)
        {
            var item = JSONObject()
            item.put("user",it.chatHistory.user2)
            item.put("icon",it.user.icon)
            item.put("date",it.chatHistory.updateDate)
            item.put("lastMessage",it.chatHistory.lastMessage)
            historiesJson.put(item)
        }

        res.put("messages", historiesJson)

        return  res.toString()
    }
}
