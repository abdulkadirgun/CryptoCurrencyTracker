package com.example.cryptocurrencytracker.ui.screens.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.cryptocurrencytracker.R
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.databinding.FragmentDetailBinding
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.hide
import com.example.cryptocurrencytracker.util.isNumber
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
                viewModel.coinDetail.collect{ coin->
                    coin?.let {
                        when (coin) {
                            is Resource.Error -> {
                                binding.mainContainer.hide()
                                binding.progressBar.hide()
                                binding.errorText.apply {
                                    show()
                                    text = coin.message
                                }
                            }

                            is Resource.Loading -> {
                                binding.mainContainer.hide()
                                binding.progressBar.show()
                            }

                            is Resource.Success -> {
                                binding.mainContainer.show()
                                binding.progressBar.hide()
                                setData(coin.data!!)
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

            //todo sadece fiyatı gğncelle
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
        }

    }

    private fun handleRefreshPriceFeature(interval: Long) {
        //to avoid multiple refresh
        refreshJob?.cancel()

        refreshJob = lifecycleScope.launch {
            while(true){
                delay(interval*1000)
                viewModel.getCoinDetail(coinId)
            }
        }
    }

    private fun setData(data: CoinDetailItem) {

        Log.d("setData", "data: $data ")

        coinItem = data.toCoinItem()
        viewModel.checkIsCoinInFav(coinItem!!)

        binding.apply {
            Glide.with(requireContext()).load(data.image.large).into(binding.coinImg)
            coinName.text = data.name
            coinSymbol.text = data.symbol
            coinCurrentPrice.text = getString(R.string.x_usd, data.market_data.current_price.usd)
            hashingAlgorithm.text = data.hashing_algorithm
            coinDescription.text = data.description.en

            val percentage = data.market_data.price_change_percentage_24h
            changePercentage.text = percentage.toString().take(5)
            if (percentage > 0) changePercentage.setTextColor(Color.GREEN) else changePercentage.setTextColor(Color.RED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshJob?.cancel()
        _binding = null
    }

}