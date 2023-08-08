package com.example.cryptocurrencytracker.ui.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.model.DetailPageState
import com.example.cryptocurrencytracker.domain.usecases.detail.GetCoinByIdUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.AddThisCoinIntoUserFavUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.CheckThisCoinIsInFavListUseCase
import com.example.cryptocurrencytracker.domain.usecases.favourite.DeleteThisCoinFromUserFavUseCase
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCoinByIdUseCase: GetCoinByIdUseCase,
    private val addThisCoinIntoUserFavUseCase: AddThisCoinIntoUserFavUseCase,
    private val deleteThisCoinFromUserFavUseCase: DeleteThisCoinFromUserFavUseCase,
    private val checkThisCoinIsInFavListUseCase: CheckThisCoinIsInFavListUseCase
) : ViewModel() {

    private val _coinDetail: MutableStateFlow<DetailPageState?> = MutableStateFlow(null)
    val coinDetail = _coinDetail.asStateFlow()

    private val _isCoinInFav : MutableStateFlow<Resource<Boolean>?> = MutableStateFlow(null)
    var isCoinInFav = _isCoinInFav.asStateFlow()


    fun checkIsCoinInFav(coinId :CoinItem) {
        viewModelScope.launch {
            checkThisCoinIsInFavListUseCase(coinId).collect{ coin->
                _isCoinInFav.update { coin }
            }
        }
    }

    fun getCoinDetail(coinId :String, isRefreshing :Boolean = false) {
        viewModelScope.launch {
            getCoinByIdUseCase(coinId).collect{ coin->
                _coinDetail.update { DetailPageState(coinDetail = coin, isRefreshing ) }
            }
        }
    }

    fun addCoinIntoFav(coinItem: CoinItem){
        viewModelScope.launch {
            addThisCoinIntoUserFavUseCase(coinItem).collect{
                // todo
                Log.d("DetailViewModel", "addThisCoinIntoUserFavUseCase: $it ")
            }
        }
    }

    fun deleteCoinFromFav(coinItem: CoinItem){
        viewModelScope.launch {
            deleteThisCoinFromUserFavUseCase(coinItem).collect{
                // todo
                Log.d("DetailViewModel", "deleteThisCoinFromUserFavUseCase: $it ")
            }
        }
    }

}