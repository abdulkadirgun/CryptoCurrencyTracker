package com.example.cryptocurrencytracker.ui.screens.detail

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.databinding.FragmentDetailBinding
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.gone
import com.example.cryptocurrencytracker.util.hide
import com.example.cryptocurrencytracker.util.isNumber
import com.example.cryptocurrencytracker.util.roundToTwoDecimal
import com.example.cryptocurrencytracker.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val coinId by lazy { DetailFragmentArgs.fromBundle(requireArguments()).coinId }
    private var refreshJob : Job? = null
    private var coinItem: CoinItem? =null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCoinDetail(coinId)

        setClicks()

        observeData()

    }

    private fun observeData() {

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.coinDetail.collect{ state->
                    state?.let {
                        when (state.coinDetail) {
                            is Resource.Error -> {
                                binding.mainContainer.gone()
                                binding.progressBar.gone()
                                binding.errorText.apply {
                                    show()
                                    text = state.coinDetail.message
                                }
                            }

                            is Resource.Loading -> {
                                if (state.refreshing){
                                    binding.coinCurrentPriceProgress.show()
                                    binding.coinCurrentPrice.hide()
                                    binding.changePercentage.hide()
                                }
                                else{
                                    binding.mainContainer.gone()
                                    binding.progressBar.show()
                                }

                            }

                            is Resource.Success -> {
                                if (state.refreshing){
                                    binding.coinCurrentPriceProgress.gone()
                                    binding.coinCurrentPrice.show()
                                    binding.changePercentage.show()
                                }
                                else{
                                    binding.mainContainer.show()
                                    binding.progressBar.gone()
                                }
                                setData(state.coinDetail.data!!, state.refreshing)
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isCoinInFav.collect{ coin->
                    coin?.let {
                        when (coin) {
                            is Resource.Error -> {}

                            is Resource.Loading -> {}

                            is Resource.Success -> {
                                binding.favBtn.isChecked = coin.data!!
                            }
                        }
                    }
                }
            }
        }
    }


    private fun setClicks() {
        binding.refreshIntervalApplyBtn.setOnClickListener {

            //todo sadece fiyatı güncelle
            if (binding.refreshIntervalEt.isNumber())
                handleRefreshPriceFeature(binding.refreshIntervalEt.text.toString().toLong())
            else
                Toast.makeText(requireContext(), "value is not valid", Toast.LENGTH_SHORT).show()
        }


        binding.favBtn.setOnCheckedChangeListener { buttonView, isChecked ->

            if (buttonView.isPressed) {
                if (isChecked)
                    viewModel.addCoinIntoFav(coinItem!!)
                else
                    viewModel.deleteCoinFromFav(coinItem!!)
            }

            /** request permission for android 13 and upper */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

    }

    private fun handleRefreshPriceFeature(interval: Long) {
        /** to avoid multiple refresh*/
        refreshJob?.cancel()

        refreshJob = lifecycleScope.launch {
            while(true){
                delay(interval*1000)
                viewModel.getCoinDetail(coinId, isRefreshing = true)
            }
        }
    }

    private fun setData(data: CoinDetailItem, refreshOnlyPrice: Boolean) {

        Log.d("setData", "data: $data ")

        coinItem = data.toCoinItem()
        viewModel.checkIsCoinInFav(coinItem!!)

        binding.apply {
            binding.coinCurrentPrice.text =
                getString(R.string.x_usd, data.market_data.current_price.usd.roundToTwoDecimal())
            val percentage = data.market_data.price_change_percentage_24h
            changePercentage.text = getString(R.string.x_percentage, percentage.toString().take(5))
            if (percentage > 0)
                changePercentage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.theGreen
                    )
                )
            else
                changePercentage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.theRed
                    )
                )
        }

        if (!refreshOnlyPrice) {
            binding.apply {
                Glide.with(requireContext()).load(data.image.large).into(binding.coinImg)
                coinName.text = data.name
                coinSymbol.text = data.symbol
                hashingAlgorithm.text = data.hashing_algorithm
                coinDescription.text = data.description.en


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshJob?.cancel()
        _binding = null
    }

}