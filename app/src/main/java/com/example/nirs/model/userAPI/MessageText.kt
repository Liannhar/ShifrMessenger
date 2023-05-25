package com.example.nirs.model.userAPI

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONString
import org.json.JSONStringer

data class MessageText (

    @SerializedName("type")
    var type: String,

    @SerializedName("sander")
    var sander: String,

    @SerializedName("message")
    var message: String,
    @SerializedName("length")
    var length:Int
)