package com.techjd.authenticationflow.ui.fragment.signup

import android.R.layout
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.contains
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.techjd.authenticationflow.R
import com.techjd.authenticationflow.databinding.FragmentSignUpBinding
import com.techjd.authenticationflow.ui.activity.MainActivity
import com.techjd.authenticationflow.util.PreferencesManager
import com.techjd.authenticationflow.util.Result.Error
import com.techjd.authenticationflow.util.Result.Idle
import com.techjd.authenticationflow.util.Result.Success
import com.techjd.authenticationflow.util.validatePassword
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignUpFragment : Fragment() {

  private var _binding: FragmentSignUpBinding? = null
  private val binding get() = _binding!!
  private val TAG = this::class.java.simpleName

  @Inject
  lateinit var preferencesManager: PreferencesManager

  val signUpViewModel: SignUpViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSignUpBinding.inflate(inflater, container, false)
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

    binding.apply {

      loginText.setOnClickListener {
        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
      }

      spinner.onItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(
          parentView: AdapterView<*>?,
          selectedItemView: View?,
          position: Int,
          id: Long
        ) {
          signUpViewModel.setSelectedCountry(position)
        }

        override fun onNothingSelected(parentView: AdapterView<*>?) {
        }
      }

      userNameEdt.doAfterTextChanged { text ->
        text?.let { input ->
          if (input.isNotBlank() && !input.trim().contains(" ")) {
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
          if(input.toString().trim().validatePassword()) {
            password.isErrorEnabled = false
          } else {
            password.error = "Password should be atleast 8 chars and contain atleast " +
              "one number, special characters[!@#\$%&()], 1 lowercase letter, and 1 uppercase letter"
            password.isErrorEnabled = true
          }
        }
      }

      register.setOnClickListener {
        val validationResult = validate()

        if (validationResult.first && validationResult.second && validationResult.third) {
          signUpViewModel.saveUser(
            binding.userNameEdt.text.toString().trim(),
            binding.passwordEdt.text.toString().trim()
          )
        } else {
          if (!validationResult.first) {
            showSnackBar("Please Enter User Name")
          } else if(!validationResult.second) {
            showSnackBar("Please Enter Password")
          } else if(!validationResult.third) {
            showSnackBar("Please Select a country")
          }
        }
      }
    }

    val arrayAdapter = ArrayAdapter(requireContext(), layout.simple_spinner_item, getCountries())
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.spinner.adapter = arrayAdapter

    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          signUpViewModel.selectedCountry.collect { selectedCountry ->
            if (selectedCountry != -1) {
              binding.spinner.setSelection(selectedCountry)
            }
          }
        }

        launch {
          signUpViewModel.saveResult.collect { result ->
            when(result) {
              is Error -> {
                result.message?.let {
                  showSnackBar(it)
                }
              }
              is Idle -> {
                // no need to do anything
              }
              is Success -> {
                preferencesManager.saveUserLoggedInValue(true)
                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                (activity as MainActivity).setStartDestinationAsHome()
              }
            }
          }
        }
      }
    }
  }

  private fun getCountries() : List<String> {
      return context?.let {  ctx ->
        val inputStream = ctx.assets?.let { asset ->
          val inputs = asset.open("countries.json")
          inputs
        }
        val buffer = inputStream?.let { stream ->
          val size = stream.available()
          val buffer = ByteArray(size)
          stream.read(buffer)
          stream.close()
          buffer
        }
        val countries = buffer?.let { byteArray ->
          val json = String(byteArray, Charsets.UTF_8)
          val gson = Gson()

          val jsonMap: Map<String, Map<String, String>> = gson.fromJson(json, Map::class.java) as Map<String, Map<String, String>>

          val dataValues: Collection<Map<String, String>> = (jsonMap["data"]?.values ?: emptyList()) as Collection<Map<String, String>>

          val dataList: List<String> = dataValues.map { map ->
            map["country"] ?: " "
          }

          (dataList as MutableList<String>).add(0, "Select a country")

          dataList
        }
        countries
      } ?: emptyList()
  }

  private fun validate(): Triple<userNameResult, passwordResult, selectCountryResult> {
    var result = Triple(false, false, false)

    val userName = binding.userNameEdt.text.toString().trim()
    val password = binding.passwordEdt.text.toString().trim()
    val selectedCountry = binding.spinner.selectedItemPosition

    if (userName.isNotBlank() && !userName.contains(" ")) {
      result = result.copy(first = true)
    }

    if(password.validatePassword()) {
      result = result.copy(second = true)
    }

    if(selectedCountry != 0) {
      result = result.copy(third = true)
    }

    return result
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("username", binding.userNameEdt.text.toString())
    outState.putString("password", binding.passwordEdt.text.toString())
  }

  private fun showSnackBar(message: String) {
    Snackbar.make(binding.constraintLayout,
      message, Snackbar.LENGTH_SHORT).show()
  }

}

typealias userNameResult = Boolean
typealias passwordResult = Boolean
typealias selectCountryResult = Boolean