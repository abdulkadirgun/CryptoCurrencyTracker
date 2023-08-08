package com.example.cryptocurrencytracker.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.usecases.auth.LogoutUserUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.AddThisCoinIntoUserFavUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinSearchResultUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinsForEachSessionUseCase
import com.example.cryptocurrencytracker.domain.usecases.home.GetCoinsFromDBUseCase
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoinsForEachSessionUseCase: GetCoinsForEachSessionUseCase,
    private val getCoinSearchResultUseCase: GetCoinSearchResultUseCase,
    private val getCoinsFromDBUseCase: GetCoinsFromDBUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val addThisCoinIntoUserFavUseCase: AddThisCoinIntoUserFavUseCase
    ) : ViewModel() {

    private val _coinList: MutableStateFlow<Resource<Any>?> = MutableStateFlow(null)
    val coinList = _coinList.asStateFlow()

    private val _logoutInfo: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(Resource.Loading())
    val logoutInfo = _logoutInfo.asStateFlow()

    private var searchJob: Job? = null


    fun getCoinForTheFirstTime(){
        Log.d("HomeViewModel", "getCoinForTheFirstTime called")
        viewModelScope.launch {
            getCoinsForEachSessionUseCase().collect{ isSuccess->
                _coinList.update { isSuccess }
            }
        }
    }

    fun getCoinsFromDB(){
        Log.d("HomeViewModel", "getCoinsFromDB called")
        viewModelScope.launch {
            getCoinsFromDBUseCase().collect{ list->
                _coinList.update { Resource.Success(list) }
            }
        }

    }

   /* fun getCoinForTheFirstTime(){
        viewModelScope.launch {
            getCoinsForEachSession().collect{ isSuccess->
                when(isSuccess){
                    is Resource.Error -> {
                        _isCoinsFetchedAndSaved.update { Resource.Error(isSuccess.message) }
                        Log.d("burdayım", "1 ${isSuccess.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("burdayım", "2")
                        _isCoinsFetchedAndSaved.update { Resource.Loading() }
                    }
                    is Resource.Success -> {
                        Log.d("burdayım", "3")
                        INITIAL_FETCH = true
                    }
                }
            }
        }.onJoin
    }*/



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

    fun logout(){
        viewModelScope.launch {
            logoutUserUseCase().collect{ logout->
                _logoutInfo.update { logout }
            }
        }
    }

}