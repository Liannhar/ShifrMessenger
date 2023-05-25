package com.example.nirs.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.nirs.model.userDB.UserDatabase
import com.example.nirs.model.userDB.UserRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.example.nirs.model.userDB.User
import kotlinx.coroutines.launch

class DBViewModel(application: Application) : AndroidViewModel(application)  {
    val userRepository : UserRepository

    init {
        val dao = UserDatabase.getDatabase(application).getUserDao()
        userRepository = UserRepository(dao)
    }

    fun deleteUser (user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.delete(user)
    }

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.update(user)
    }


    fun addUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.insert(user)
    }

    fun getUser(id:Long) = userRepository.getOneUser(id)


}