package com.example.nirs.model.userAPI

import com.google.gson.annotations.SerializedName
import kotlin.String

data class UserAPI(
    @SerializedName("id")
    var id: String,

    @SerializedName("nickname")
    var nickname: String,

    @SerializedName("password")
    var password: String,
)