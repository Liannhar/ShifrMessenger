package com.example.nirs.accessors


import com.example.nirs.model.userAPI.Message
import com.example.nirs.model.userAPI.MessageText
import com.example.nirs.model.userAPI.Room
import com.example.nirs.model.userAPI.UserAPI
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    @GET("users")
    fun getAllUsers(): Call<List<UserAPI>>

    @GET("rooms")
    fun getRoom(@Query("myNickname") myNickname: String?,@Query("nickname") nickname: String?): Call<Room>

    @GET("messages")
    fun getAllMessages(@Query("id") id: Int): Call<List<MessageText>>
}