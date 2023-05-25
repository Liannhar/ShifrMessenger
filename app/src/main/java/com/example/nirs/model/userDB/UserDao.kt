package com.example.nirs.model.userDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User):Long

    @Delete
    fun delete(user:User)

    @Update
    fun update(user: User)

    @Query("Select * from userTable order by id ASC")
    fun getAllUser(): Flow<List<User>>

    @Query("Select * from userTable Where id=:id")
    fun getOneUser(id:Long): Flow<User>
}