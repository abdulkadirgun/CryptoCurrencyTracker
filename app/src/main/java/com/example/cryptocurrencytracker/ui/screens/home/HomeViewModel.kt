package com.example.cryptocurrencytracker.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinsAndSaveDBUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinSearchResultUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinsFromDBUseCase
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.multibindings.ElementsIntoSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoinsAndSaveDBUseCase: GetCoinsAndSaveDBUseCase,
    private val getCoinSearchResultUseCase: GetCoinSearchResultUseCase,
    private val getCoinsFromDBUseCase: GetCoinsFromDBUseCase
) : ViewModel() {


    private val _coinList: MutableStateFlow<Resource<List<CoinEntity>>> = MutableStateFlow(Resource.Loading())
    val coinList = _coinList.asStateFlow()


    private var searchJob: Job? = null


    fun getCoinList(){
        viewModelScope.launch {
            getCoinsAndSaveDBUseCase().collect{ isSuccess->
                when(isSuccess){
                    is Resource.Error -> {
                        _coinList.update {
                            Log.d("burdayım", "1")
                            Resource.Error(isSuccess.message)
                        }
                    }
                    is Resource.Loading -> {
                        Log.d("burdayım", "2")

                    }
                    is Resource.Success -> {
                        Log.d("burdayım", "3")
                        if (isSuccess.data!!)
                            getCoinsFromDB()
                    }
                }
            }
        }
    }

    private fun getCoinsFromDB(){
        viewModelScope.launch {
            getCoinsFromDBUseCase().collect{ list->

                _coinList.update {
                    Resource.Success(list)

                }
                Log.d("burdayım", "4")

            }
            cancel()
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