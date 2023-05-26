package com.example.nirs.activity



import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nirs.Encryption
import com.example.nirs.R
import com.example.nirs.Server
import com.example.nirs.accessors.ApiClient
import com.example.nirs.accessors.ApiService
import com.example.nirs.adapters.ChatAdapter
import com.example.nirs.model.userAPI.Message
import com.example.nirs.model.userAPI.MessageText
import com.example.nirs.model.userAPI.Room
import io.socket.engineio.parser.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream



class ChatActivity : AppCompatActivity() {
    private var roomId:Int = 0

    val adapter = ChatAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)
        val server= Server()
        val nicknameTopPanel = findViewById<TextView>(R.id.nickname_chat)
        val backButton = findViewById<ImageView>(R.id.back_button_chat)
        val sendButton = findViewById<ImageButton>(R.id.send_button)
        val imageSandButton = findViewById<ImageButton>(R.id.image_button)
        val editText = findViewById<EditText>(R.id.chat_input)
        val recyclerView = findViewById<RecyclerView>(R.id.chat_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var messages = ""
        recyclerView.adapter = adapter

        server.connectToServer()

        val nickname = intent.getStringExtra("nickname").orEmpty()
        val myNickname = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).getString("nickname","")
        nicknameTopPanel.text = nickname
        if (myNickname != null) {
            lifecycleScope.launch {
                server.join(myNickname,nickname)
                textsHistory(myNickname,nickname)
            }
        }
        server.returnSocketForListening().on("private_chat") { args ->
            val sander = args[2] as String
            messages = args[1] as String
            val length = args[3] as Int
            val type = args[0] as String

            lifecycleScope.launch(Dispatchers.Main) {
                myNickname?.let { setItemsAdapter(sander, it,messages,type,length) }
            }
        }

        sendButton.setOnClickListener {
            val message = editText.text.toString()
            editText.text.clear()
            myNickname?.let { server.sendMessage(it, nickname, message, "String",message.length) }
        }

        val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            lifecycleScope.launch(Dispatchers.IO){
                val message = editText.text.toString()
                editText.text.clear()
                val bitmapString = bitmap?.let { encoder(it,message) }
                val oldBitmapString = bitmap?.let { encoderOld(it) }
                myNickname?.let {
                    if (bitmapString != null) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            oldBitmapString?.let { it1 ->
                                setItemsAdapter(it, it,
                                    it1,"images",message.length)
                            }
                        }
                        server.sendMessage(it,nickname,bitmapString,"Image",message.length)
                    }
                }
            }

        }

        imageSandButton.setOnClickListener {
                takePicture.launch(null)
        }

        backButton.setOnClickListener {
            server.closeServer()

            finish()
        }
    }

    private fun encoderOld(bitmap: Bitmap):String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun textsHistory(myNickname:String,nickname: String){
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val callRooms = apiService.getRoom(myNickname,nickname)
        val progressBar = findViewById<CardView>(R.id.progressBar_chat)
        progressBar.isVisible = true
        callRooms.enqueue(object : Callback<Room> {
            override fun onResponse(call: Call<Room>, response: Response<Room>) {
                if (response.isSuccessful) {
                    val rooms = response.body()
                    roomId = rooms?.id ?: 0
                    val callMessage = apiService.getAllMessages(roomId)
                    callMessage.enqueue(object : Callback<List<MessageText>> {
                        override fun onResponse(
                            call: Call<List<MessageText>>,
                            response: Response<List<MessageText>>
                        ) {
                            if (response.isSuccessful) {
                                val messages = response.body()
                                messages?.forEach {
                                    val message = JSONObject(it.message)
                                    setItemsAdapter(
                                        it.sander,
                                        myNickname,
                                        message.getString("message"),
                                        it.type,
                                        it.length
                                    )
                                }
                                progressBar.isVisible = false
                            } else {
                                Log.i("WINWIN", "Error")
                                progressBar.isVisible = false
                            }
                        }

                        override fun onFailure(call: Call<List<MessageText>>, t: Throwable) {
                            // Handle the failure
                            progressBar.isVisible = false
                        }
                    })
                } else {
                    Log.i("WINWIN", "Error")
                    progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<Room>, t: Throwable) {
                // Handle the failure
                progressBar.isVisible = false
            }
        })
    }

    private fun setItemsAdapter(nickname: String, myNickname: String, message: String, type: String,length:Int)
    {
        if (nickname == myNickname) {
            adapter.addMessage(
                Message(
                    nickname,
                    message,
                    true,
                    type,
                    length
                )
            )
        } else {
            adapter.addMessage(
                Message(
                    nickname,
                    message,
                    false,
                    type,
                    length
                )
            )
        }
    }

    // Encode Image to Base64
    private fun encoder(bitmap: Bitmap,message: String): String {
        val encode = Encryption()
        val newImage = encode.encodeImage(bitmap,message)
        val byteArrayOutputStream = ByteArrayOutputStream()
        newImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}