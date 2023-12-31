package com.example.cryptocurrencytracker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cryptocurrencytracker.databinding.ActivityMainBinding
import com.example.cryptocurrencytracker.util.gone
import com.example.cryptocurrencytracker.util.show
import dagger.hilt.android.AndroidEntryPoint


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
                binding.bottomNavView.gone()
            else
                binding.bottomNavView.show()
        }

        mainActivityViewModel.scheduleWorksToSyncAppWithBackend()

    }

}