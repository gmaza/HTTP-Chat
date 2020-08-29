package com.example.chatserver.Domain.UseCases

import com.example.chatserver.Common.models.ResultModel
import com.example.chatserver.Domain.Repositories.UsersRepository
import com.example.chatserver.Domain.User.User
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

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

data class GetUsersResponse(val users: List<User>, val quantity: Int){
    fun ToJson() : String {
        var res = JSONObject()

        res.put("quantity", quantity)

        var usersJson = JSONArray()
        for(it in users)
        {
            var item = JSONObject()
            item.put("name",it.name)
            item.put("profession",it.profession)
            item.put("icon",it.icon)
            usersJson.put(item)
        }

        res.put("users", usersJson)

        return  res.toString()
    }
}