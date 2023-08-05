package com.example.cryptocurrencytracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinItem
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoCurrencyRepository: CryptoCurrencyRepository
) : ViewModel() {

    private val _coinList: MutableStateFlow<Resource<List<CoinEntity>>?> = MutableStateFlow(null)
    val coinList = _coinList.asStateFlow()

    fun getCoinList(){
        viewModelScope.launch {
            cryptoCurrencyRepository.getCoinList().collect{ list->
                _coinList.update { list }
            }
        }
    }}