package com.techjd.authenticationflow.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.techjd.authenticationflow.R
import com.techjd.authenticationflow.databinding.FragmentLoginBinding
import com.techjd.authenticationflow.ui.activity.MainActivity
import com.techjd.authenticationflow.util.PreferencesManager
import com.techjd.authenticationflow.util.Result.Error
import com.techjd.authenticationflow.util.Result.Idle
import com.techjd.authenticationflow.util.Result.Success
import com.techjd.authenticationflow.util.validatePassword
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!
  private val TAG = this::class.java.simpleName

  private val loginViewModel: LoginViewModel by viewModels()

  @Inject
  lateinit var preferencesManager: PreferencesManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentLoginBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState != null) {
      with(binding) {
        passwordEdt.setText(savedInstanceState.getString("username"))
        userNameEdt.setText(savedInstanceState.getString("password"))
      }
    }

    with(binding) {

      userNameEdt.doAfterTextChanged { text ->
        text?.let { input ->
          if (input.isNotBlank() && !input.contains(" ")) {
            // do not show anything
            userName.isErrorEnabled = false
          } else {
            userName.error = "User Name should not have any white spaces"
            userName.isErrorEnabled = true
          }
        }
      }

      passwordEdt.doAfterTextChanged { text ->
        text?.let { input ->
          if(input.toString().validatePassword()) {
            password.isErrorEnabled = false
          } else {
            password.error = "Password should be atleast 8 chars and contain atleast " +
              "one number, special characters[!@#\$%&()], 1 lowercase letter, and 1 uppercase letter"
            password.isErrorEnabled = true
          }
        }
      }

      register.setOnClickListener {
        findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
      }

      login.setOnClickListener {
        val validationResult = validate()

        if (validationResult.first && validationResult.second) {
          loginViewModel.loginUser(
            userName =  binding.userNameEdt.text.toString().trim(),
            password =  binding.passwordEdt.text.toString().trim()
          )
        } else {
          if (!validationResult.first) {
            showSnackBar("Please Enter User Name")
          } else if(!validationResult.second) {
            showSnackBar("Please Enter Password")
          }
        }
      }

      viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
          loginViewModel.loginResult.collect { result ->
            when(result) {
              is Error -> {
                result.message?.let {
                  showSnackBar(it)
                }
              }
              is Idle -> {
                // no operation
              }
              is Success -> {
                preferencesManager.saveUserLoggedInValue(true)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                (activity as MainActivity).setStartDestinationAsHome()
              }
            }
          }
        }
      }
    }
  }

  private fun validate(): Pair<userNameResult, passwordResult> {
    var result = Pair(false, false)

    val userName = binding.userNameEdt.text.toString().trim()
    val password = binding.passwordEdt.text.toString().trim()

    if (userName.isNotBlank() && !userName.contains(" ")) {
      result = result.copy(first = true)
    }

    if(password.validatePassword()) {
      result = result.copy(second = true)
    }

    return result
  }

  private fun showSnackBar(message: String) {
    Snackbar.make(binding.constraintLayout,
      message, Snackbar.LENGTH_SHORT).show()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("username", binding.userNameEdt.text.toString())
    outState.putString("password", binding.passwordEdt.text.toString())
  }

}

typealias userNameResult = Boolean
typealias passwordResult = Boolean