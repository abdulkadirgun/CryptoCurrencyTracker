package com.example.cryptocurrencytracker.ui.screens.favourites

import android.os.Bundle
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
import com.example.cryptocurrencytracker.databinding.FragmentFavouritesBinding
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.ui.common.FavouritesAdapter
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.gone
import com.example.cryptocurrencytracker.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavCoins()

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.coinList.collect { coins ->
                    coins?.let {
                        when (coins) {
                            is Resource.Error -> {
                                binding.rcv.gone()
                                binding.progressBar.gone()
                                binding.errorText.apply {
                                    show()
                                    text = coins.message
                                }
                            }

                            is Resource.Loading -> {
                                binding.rcv.gone()
                                binding.progressBar.show()
                            }

                            is Resource.Success -> {
                                binding.rcv.show()
                                binding.progressBar.gone()
                                setData(coins.data)
                            }
                        }
                    }
                }
            }
        }


    }

    private fun setData(data: List<CoinItem>?) {
        binding.rcv.apply {

            adapter = FavouritesAdapter(data as ArrayList<CoinItem>, requireContext()){
                findNavController().navigate(
                    FavouritesFragmentDirections.actionFavouritesFragmentToDetailFragment(
                        it
                    )
                )
            }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}