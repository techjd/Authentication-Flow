package com.techjd.authenticationflow.data.repository

import com.techjd.authenticationflow.data.local.dao.UserDao
import com.techjd.authenticationflow.data.local.entity.User
import javax.inject.Inject

class UserRepository @Inject constructor(
  private val userDao: UserDao
) {

  suspend fun getUser(
    userName: String,
    password: String
  ) = userDao.getUserDetails(userName, password)

  suspend fun saveUser(user: User) = userDao.saveUser(user)

  suspend fun doesUserExists(userName: String) = userDao.doesUserExists(userName)
}