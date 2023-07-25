package com.techjd.authenticationflow.util

sealed class Result<T>(val data: T?, val message: String?) {
  class Success<T>(data: T) : Result<T>(data, null)
  class Error<T>(message: String?, data: T? = null) :
    Result<T>(null, message)
  class Idle<T>(): Result<T>(null, null)
}