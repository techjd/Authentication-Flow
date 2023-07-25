package com.techjd.authenticationflow.ui.fragment.signup

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
class SignUpViewModel @Inject constructor(
  private val userRepository: UserRepository
): ViewModel() {

  private val _countries: MutableStateFlow<List<String>> = MutableStateFlow(
    mutableListOf("Select a Country")
  )
  val countries: StateFlow<List<String>> = _countries

  private val _selectedCountry: MutableStateFlow<Int> = MutableStateFlow(0)
  val selectedCountry: StateFlow<Int> = _selectedCountry

  private var _saveResult: MutableStateFlow<Result<String>> = MutableStateFlow(Result.Idle())
  val saveResult: StateFlow<Result<String>> = _saveResult

  fun setSelectedCountry(index: Int) {
    _selectedCountry.value = index
  }

  fun saveUser(userName: String, password: String) {
    val user = User(userName = userName, password = password, countryIndex = selectedCountry.value)
    viewModelScope.launch {
      val doesUserExists = userRepository.doesUserExists(user.userName)
      if (doesUserExists.isNotEmpty()) {
        _saveResult.value = Result.Error("User Already Exists")
      } else {
        userRepository.saveUser(user)
        _saveResult.value = Result.Success("Success")
      }
    }
  }
}