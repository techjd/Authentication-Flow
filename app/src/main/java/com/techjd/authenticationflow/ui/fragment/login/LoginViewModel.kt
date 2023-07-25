package com.techjd.authenticationflow.ui.fragment.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techjd.authenticationflow.data.local.entity.User
import com.techjd.authenticationflow.data.repository.UserRepository
import com.techjd.authenticationflow.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val userRepository: UserRepository
): ViewModel() {
  private val _loginResult: MutableStateFlow<Result<Unit>> = MutableStateFlow(Result.Idle())
  val loginResult: StateFlow<Result<Unit>> = _loginResult

  fun loginUser(userName: String, password: String) {
    viewModelScope.launch {
      val doesUserExists = userRepository.doesUserExists(userName)
      if (doesUserExists.isNotEmpty()) {
        val user = userRepository.getUser(userName, password)
        if (user.isNotEmpty()) {
          _loginResult.value = Result.Success(Unit)
        } else {
          _loginResult.value = Result.Error("Details don't match")
        }
      } else {
        _loginResult.value = Result.Error("Account doesn't exists")
      }
    }
  }
}