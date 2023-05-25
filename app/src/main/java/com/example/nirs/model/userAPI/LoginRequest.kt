package com.example.nirs.model.userAPI

import com.google.gson.annotations.SerializedName
import kotlin.String

data class LoginRequest (
    @SerializedName("nickname")
    var nickname: String,

    @SerializedName("password")
    var password: String,
)