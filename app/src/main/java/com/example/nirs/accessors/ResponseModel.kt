package com.example.nirs.accessors

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String
)
