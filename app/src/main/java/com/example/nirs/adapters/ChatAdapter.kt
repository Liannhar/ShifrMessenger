package com.example.nirs.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nirs.Encryption
import com.example.nirs.R
import com.example.nirs.model.userAPI.Message
import io.socket.engineio.parser.Base64

class ChatAdapter(private var messages: ArrayList<Message> = arrayListOf()
) : RecyclerView.Adapter<ChatAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val nickname = view.findViewById<TextView>(R.id.chat_item_username)
        private val message = view.findViewById<TextView>(R.id.chat_item_message)
        private val form = view.findViewById<CardView>(R.id.chat_item_gravity)
        private val image = view.findViewById<ImageView>(R.id.chat_item_image)
        fun bind(item: Message) {
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(12, 4, 12, 4)

            nickname.text = item.nickname
            when(item.type){
                "String"->
                {
                    message.visibility = View.VISIBLE
                    message.text = item.message

                }
                "Image"-> {
                    image.visibility = View.VISIBLE
                    message.visibility = View.VISIBLE
                    val bitmap = convertToBitmap(decoder(item.message))
                    message.text = Encryption().decodeImage(bitmap,item.length)
                    Log.i("WINWIN", item.length.toString())
                    Glide.with(itemView.context).load(bitmap).fitCenter().into(image)
                }
                "images"->{
                    image.visibility = View.VISIBLE
                    message.visibility = View.VISIBLE
                    val bitmap = convertToBitmap(decoder(item.message))
                    message.text = "Original"
                    Glide.with(itemView.context).load(bitmap).fitCenter().into(image)
                }

            }

            if (item.isMe){
                params.gravity = Gravity.END
                form.layoutParams = params
            }
            else{
                params.gravity = Gravity.START
                form.layoutParams = params
                form.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.blind_orange))
            }
        }
        private fun decoder(base64Str: String): ByteArray {
            val pureBase64Encoded = base64Str.substring(base64Str.indexOf(",") + 1)
            return Base64.decode(pureBase64Encoded, Base64.DEFAULT)
        }
        private fun convertToBitmap(imageData:ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bind(messages[position])

    override fun getItemCount(): Int = messages.size

    fun submitItems(data: ArrayList<Message>) {
        messages = data
        notifyItemRangeChanged(0, messages.size - 1)
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun addListMessages(messageList: List<Message>) {
        this.messages.addAll(messageList)
        notifyItemRangeInserted(this.messages.size - messages.size, messages.size)
    }


    fun changeAllMessages(newMessages:ArrayList<Message>){
        messages=newMessages
        notifyDataSetChanged()
    }

}