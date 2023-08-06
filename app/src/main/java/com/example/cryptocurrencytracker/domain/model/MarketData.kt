package com.example.cryptocurrencytracker.domain.model

import com.example.cryptocurrencytracker.domain.model.CurrentPrice

data class MarketData(
    val current_price: CurrentPrice,
    val last_updated: String,
    val price_change_percentage_24h: Double
)