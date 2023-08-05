package com.example.cryptocurrencytracker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto.CoinDetailItem
import com.example.cryptocurrencytracker.domain.usecases.api.GetCoinByIdUseCase
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCoinByIdUseCase: GetCoinByIdUseCase
) : ViewModel() {

    private val _coinDetail: MutableStateFlow<Resource<CoinDetailItem>?> = MutableStateFlow(null)
    val coinDetail = _coinDetail.asStateFlow()

    fun getCoinDetail(coinId :String) {
        viewModelScope.launch {
            getCoinByIdUseCase(coinId).collect{ coin->
                _coinDetail.update { coin }
            }
        }
    }

}