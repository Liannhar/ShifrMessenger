package com.example.nirs.model.userDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
class User (
    @PrimaryKey val id:Long,
    @ColumnInfo(name = "username") var username :String,
)
{
}
