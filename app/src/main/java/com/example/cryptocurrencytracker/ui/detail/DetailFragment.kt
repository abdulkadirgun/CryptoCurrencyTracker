package com.example.cryptocurrencytracker.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinDetailItem
import com.example.cryptocurrencytracker.databinding.FragmentDetailBinding
import com.example.cryptocurrencytracker.ui.BasicAdapter
import com.example.cryptocurrencytracker.ui.favourites.FavouritesViewModel
import com.example.cryptocurrencytracker.ui.home.HomeFragmentDirections
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.hide
import com.example.cryptocurrencytracker.util.show
import com.google.api.LogDescriptor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val coinId by lazy { DetailFragmentArgs.fromBundle(requireArguments()).coinId }

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


    }
    private fun setData(data: CoinDetailItem) {


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}