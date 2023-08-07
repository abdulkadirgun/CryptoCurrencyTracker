package com.example.cryptocurrencytracker.ui.screens.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.databinding.FragmentHomeBinding
import com.example.cryptocurrencytracker.ui.common.HomeAdapter
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.hide
import com.example.cryptocurrencytracker.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCoinList()

        // todo save state
        handleEdittextChanged()

        // todo swipe refresh layout

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.coinList.collect { coins ->
                    coins?.let {
                        when (coins) {
                            is Resource.Error -> {
                                binding.rcv.hide()
                                binding.progressBar.hide()
                                binding.errorText.apply {
                                    show()
                                    text = coins.message
                                }
                            }

                            is Resource.Loading -> {
                                binding.rcv.hide()
                                binding.progressBar.show()
                            }

                            is Resource.Success -> {
                                binding.rcv.show()
                                binding.progressBar.hide()
                                setData(coins.data)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setData(data: List<CoinEntity>?) {
        binding.rcv.apply {

            //todo add anim
            adapter = HomeAdapter(data as ArrayList<CoinEntity>, requireContext()){
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        it
                    )
                )
            }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleEdittextChanged() {
        binding.searchCoinEt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (s.isNotBlank()) {
                        Log.d("searchCoinEt", "istek atıldı")
                        viewModel.getSearchResult(s.toString())
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}