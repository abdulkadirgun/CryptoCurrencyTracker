package com.example.cryptocurrencytracker.domain.usecases.home

import android.util.Log
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.usecases.favourite.GetFavCoinsUseCase
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateFavCoinsPriceUseCase @Inject constructor(
    private val getFavCoinsUseCase: GetFavCoinsUseCase
    )
{

    suspend operator fun invoke(priceChangedCoinListWillBeRefresh: List<CoinDetailItem>) {

    }
}