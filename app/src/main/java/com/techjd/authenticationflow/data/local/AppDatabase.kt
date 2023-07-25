package com.techjd.authenticationflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techjd.authenticationflow.data.local.dao.UserDao
import com.techjd.authenticationflow.data.local.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}