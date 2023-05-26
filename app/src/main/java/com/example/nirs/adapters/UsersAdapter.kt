package com.example.nirs.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nirs.R
import com.example.nirs.activity.ChatActivity
import com.example.nirs.model.userAPI.UserAPI

class UsersAdapter(
    private var users: ArrayList<UserAPI> = arrayListOf()
) : RecyclerView.Adapter<UsersAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val context = view.context
        private val nickname = view.findViewById<TextView>(R.id.nickname_text_view)
        private val card = view.findViewById<CardView>(R.id.card_nickname)
        fun bind(item: UserAPI) {
            nickname.text = item.nickname
            card.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("nickname",item.nickname)
                startActivity(context,intent,null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bind(users[position])

    override fun getItemCount(): Int = users.size

    fun submitItems(data: ArrayList<UserAPI>) {
        users = data
        notifyItemRangeChanged(0, users.size - 1)
    }

    fun addUser(user: UserAPI) {
        users.add(user)
        notifyItemInserted(users.size - 1)
    }

    fun addListUsers(userList: List<UserAPI>) {
        this.users.addAll(userList)
        notifyItemRangeInserted(this.users.size - users.size, users.size)
    }

    private fun getUserPosition(user: UserAPI): Int {
        for (i in 0 until users.size)
            if (user.id == users[i].id)
                return i
        return -1
    }

    fun changeAllUsers(newUsers:ArrayList<UserAPI>){
        users=newUsers
        notifyDataSetChanged()
    }

    fun changeUser(user: UserAPI) {
        val position = getUserPosition(user)
        if (position == -1)
            return
        users[position] = user
        notifyItemChanged(position)
    }
}