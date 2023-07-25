package com.techjd.authenticationflow.util

fun String.validatePassword(): Boolean {
  val pattern = "^(?=.*[0-9])(?=.*[!@#\$%&()])(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{8,}$".toRegex()
  return pattern.matches(this)
}