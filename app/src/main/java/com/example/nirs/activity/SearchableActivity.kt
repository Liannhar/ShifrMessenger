package com.example.nirs.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nirs.R
import com.example.nirs.accessors.ApiClient
import com.example.nirs.accessors.ApiService
import com.example.nirs.adapters.UsersAdapter
import com.example.nirs.model.userAPI.Room
import com.example.nirs.model.userAPI.UserAPI
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchableActivity : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView
    val userList = arrayListOf<UserAPI>()
    lateinit var adapter:UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            fetchUsers()
        }
        // initialise ListView with id
        recyclerView = findViewById(R.id.users_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter(userList)
        recyclerView.adapter = adapter
        val searchView = findViewById<SearchView>(R.id.search)

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun fetchUsers() {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val callUsers = apiService.getAllUsers()
        val progressBar = findViewById<CardView>(R.id.progressBar_main)
        progressBar.isVisible = true
        callUsers.enqueue(object : Callback<List<UserAPI>> {
            override fun onResponse(call: Call<List<UserAPI>>, response: Response<List<UserAPI>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        userList.addAll(users)
                        adapter.changeAllUsers(userList)
                        progressBar.isVisible = false
                    }
                } else {
                    progressBar.isVisible = false
                    Log.i("WINWIN","Error")
                }
            }

            override fun onFailure(call: Call<List<UserAPI>>, t: Throwable) {
                // Handle the failure
            }
        })


    }


    private fun filterList(newText: String) {
        val filteredList = ArrayList<UserAPI>()
        for (user in userList){
            if (user.nickname.lowercase().contains(newText.lowercase()))
            {
                filteredList.add(user)
            }
        }
        if (filteredList.isEmpty()) Toast.makeText(this,"No data Found",Toast.LENGTH_SHORT).show()
        else adapter.changeAllUsers(filteredList)
    }
}