package com.example.nirs.model.userAPI

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("auth_token")
    var authToken: kotlin.String,

    @SerializedName("user")
    var user: String
)