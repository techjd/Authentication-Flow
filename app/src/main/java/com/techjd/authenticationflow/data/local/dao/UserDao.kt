package com.techjd.authenticationflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techjd.authenticationflow.data.local.entity.User

@Dao
interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveUser(user: User)

  @Query("SELECT * from user WHERE username = :userName AND password = :password")
  suspend fun getUserDetails(userName: String, password: String): List<User>

  @Query("SELECT * FROM user WHERE username = :userName")
  suspend fun doesUserExists(userName: String) : List<User>
}