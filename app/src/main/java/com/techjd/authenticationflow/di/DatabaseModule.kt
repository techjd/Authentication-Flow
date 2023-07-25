package com.techjd.authenticationflow.di

import android.content.Context
import androidx.room.Room
import com.techjd.authenticationflow.data.local.AppDatabase
import com.techjd.authenticationflow.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

  @Singleton
  @Provides
  fun providesAppDatabase(
    @ApplicationContext app: Context
  ) = Room.databaseBuilder(
    app,
    AppDatabase::class.java,
    "users"
  ).build()

  @Singleton
  @Provides
  fun providesCartDao(db: AppDatabase): UserDao {
    return db.userDao()
  }

}