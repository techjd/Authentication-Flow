package com.techjd.authenticationflow.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesManager @Inject constructor(
  @ApplicationContext val context: Context
) {
  private val sharedPref = context.getSharedPreferences(
    "my_app",
    Context.MODE_PRIVATE)

  private val LOGGEDIN = "isLoggedIn"

  fun saveUserLoggedInValue(isLoggedIn: Boolean) {
    sharedPref.edit().putBoolean(LOGGEDIN, isLoggedIn).apply()
  }

  fun getUserLoggedInValue(): Boolean {
    return sharedPref.getBoolean(LOGGEDIN, false)
  }
}