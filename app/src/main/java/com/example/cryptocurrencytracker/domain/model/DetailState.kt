package com.example.cryptocurrencytracker.domain.model

import com.example.cryptocurrencytracker.util.Resource


data class DetailPageState(
    val coinDetail : Resource<CoinDetailItem>,
    val refreshing : Boolean
)
