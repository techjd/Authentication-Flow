package com.techjd.authenticationflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  @ColumnInfo(name = "username")
  val userName: String,
  @ColumnInfo(name = "password")
  val password: String,
  @ColumnInfo(name = "country")
  val countryIndex: Int
)
