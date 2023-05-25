package com.example.nirs.model.userAPI

import java.util.Base64

data class Message(
    var nickname: String,
    var message: String,
    var isMe:Boolean,
    var type:String,
    var length:Int
)