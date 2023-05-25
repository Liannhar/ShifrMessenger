package com.example.nirs.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.example.nirs.R
import com.example.nirs.accessors.ApiClient
import com.example.nirs.accessors.ApiService
import com.example.nirs.accessors.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity(): AppCompatActivity()  {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auto)

        val progressBar = findViewById<CardView>(R.id.progressBar)

        val prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE)
        val login = prefs.getInt("login", -1)

        if (login==-1) {
            findViewById<Button>(R.id.login_button).setOnClickListener {
                val nickname = findViewById<EditText>(R.id.nickname_input).text.toString()
                val password = findViewById<EditText>(R.id.password_input).text.toString()
                progressBar.isVisible=true
                apiService = ApiClient.getClient().create(ApiService::class.java)
                loginUser(nickname,password,prefs)
            }
        }
        else{
            startMActivity()
        }

    }

    private fun loginUser(username: String, password: String,prefs:SharedPreferences) {

        val call = apiService.loginUser(username, password)
        call.enqueue(object : Callback<ResponseModel> {
            val incorrect:TextView = findViewById(R.id.incorrect_text)
            val progressBar = findViewById<CardView>(R.id.progressBar)
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    prefs.edit().putInt("login",1).apply()
                    prefs.edit().putString("nickname",username).apply()
                    progressBar.isVisible=false
                    startMActivity()
                } else {
                    progressBar.isVisible=false
                    incorrect.isVisible= true
                }

            }


            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                // Обработка ошибки сети
            }
        })
    }
    private fun startMActivity(){
        val intent = Intent(this@LoginActivity, SearchableActivity::class.java)
        startActivity(intent)
        finish()
    }
}