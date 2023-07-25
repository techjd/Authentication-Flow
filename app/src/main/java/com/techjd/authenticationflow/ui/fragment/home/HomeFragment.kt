package com.techjd.authenticationflow.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.techjd.authenticationflow.R
import com.techjd.authenticationflow.databinding.FragmentHomeBinding
import com.techjd.authenticationflow.ui.activity.MainActivity
import com.techjd.authenticationflow.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!
  private val TAG = this::class.java.simpleName

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
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.logout.setOnClickListener {
      preferencesManager.saveUserLoggedInValue(false)
      findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
      (activity as MainActivity).setStartDestinationAsLogin()
    }
  }

}