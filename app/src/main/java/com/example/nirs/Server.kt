package com.example.nirs


import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject


class Server() {
    private val socket: Socket = IO.socket("https://sendp-production.up.railway.app")

    fun connectToServer(){
        socket.connect()
    }
    fun join(myNickname:String,secondNickname:String){
        socket.emit("joinRoom",JSONObject()
            .put("first", myNickname)
            .put("second",secondNickname))
    }
    fun destroyed() {
        socket.off("connect_error")
    }

    fun sendMessage(nickname: String, to: String, message: String, type: String,length:Int){
        socket.emit("private_chat", JSONObject()
            .put("first", nickname)
            .put("second",to)
            .put("message", message)
            .put("type",type)
            .put("length",length))
    }

    fun returnSocketForListening():Socket{
        return socket
    }
    /*fun takeMessage(){
        socket.on("private_chat") { args ->
            val data = args[0] as JSONObject
            val message = data.getString("message")
        }
    }*/
    fun closeServer()
    {
        socket.disconnect()
    }
}