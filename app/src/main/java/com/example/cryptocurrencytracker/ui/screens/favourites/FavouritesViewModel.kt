package com.example.cryptocurrencytracker.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytracker.domain.model.CoinItem
import com.example.cryptocurrencytracker.domain.usecases.favourite.GetFavCoinsUseCase
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavCoinsUseCase: GetFavCoinsUseCase
) : ViewModel() {

    private val _coinList : MutableStateFlow<Resource<MutableList<CoinItem>>?> = MutableStateFlow(null)
    var coinList = _coinList.asStateFlow()


    fun getFavCoins() {
        viewModelScope.launch {
            getFavCoinsUseCase().collect{ coins->
                _coinList.update{ coins }
            }
        }
    }

}
