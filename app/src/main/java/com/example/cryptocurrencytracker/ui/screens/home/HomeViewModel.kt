package com.example.cryptocurrencytracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinListUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinSearchResultUseCase
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoinListUseCase: GetCoinListUseCase,
    private val getCoinSearchResultUseCase: GetCoinSearchResultUseCase
) : ViewModel() {

    private val _coinList: MutableStateFlow<Resource<List<CoinEntity>>?> = MutableStateFlow(null)
    val coinList = _coinList.asStateFlow()

    private var searchJob: Job? = null


    fun getCoinList(){
        viewModelScope.launch {
            getCoinListUseCase().collect{ list->
                _coinList.update { list }
            }
        }
    }

    fun getSearchResult(coinName: String){
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _coinList.update { Resource.Loading() }
            delay(500L)
            getCoinSearchResultUseCase(coinName).collect{ list->
                _coinList.update { Resource.Success(list) }
            }
        }
    }

}