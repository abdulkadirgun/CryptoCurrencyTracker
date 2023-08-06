package com.example.cryptocurrencytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cryptocurrencytracker.databinding.ActivityMainBinding
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.hide
import com.example.cryptocurrencytracker.util.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainActivityViewModel : MainActivityViewModel by viewModels()
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.setupWithNavController(navController)

        val bottomNavViewIsNotVisibleList = arrayListOf(
            R.id.loginFragment,
            R.id.registerFragment,
            R.id.detailFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (bottomNavViewIsNotVisibleList.contains(destination.id))
                binding.bottomNavView.hide()
            else
                binding.bottomNavView.show()
        }

    }

}