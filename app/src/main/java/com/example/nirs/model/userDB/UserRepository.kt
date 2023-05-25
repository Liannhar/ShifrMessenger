package com.example.nirs.model.userDB

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUser: Flow<List<User>> = userDao.getAllUser()

    fun insert(user: User):Long {
        return userDao.insert(user)
    }

    fun delete(user: User){
        userDao.delete(user)
    }

    fun update(user: User){
        userDao.update(user)
    }

    fun getOneUser(id: Long): Flow<User> {
        return userDao.getOneUser(id)
    }

}