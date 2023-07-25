package com.techjd.authenticationflow.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.techjd.authenticationflow.R
import com.techjd.authenticationflow.R.id
import com.techjd.authenticationflow.R.navigation
import com.techjd.authenticationflow.databinding.ActivityMainBinding
import com.techjd.authenticationflow.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var navGraph: NavGraph
  private lateinit var navController: NavController

  @Inject
  lateinit var preferencesManager: PreferencesManager
  
  private val TAG = this::class.java.simpleName

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val navHostFragment =
      supportFragmentManager.findFragmentById(id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController
    navGraph = navController.navInflater.inflate(navigation.nav_graph)

    if (preferencesManager.getUserLoggedInValue()) {
      setStartDestinationAsHome()
    }

  }

  internal fun setStartDestinationAsHome() {
    navGraph.setStartDestination(R.id.homeFragment)
    navController.graph = navGraph
  }

  internal fun setStartDestinationAsLogin() {
    navGraph.setStartDestination(R.id.loginFragment)
    navController.graph = navGraph
  }
}