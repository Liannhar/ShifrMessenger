package com.example.nirs.model.userAPI

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class Room(
    @SerializedName("id")
    var id:Int,

    @SerializedName("firstNickname")
    var firstNickname: String,

    @SerializedName("nickname")
    var secondNickname: String
)