package com.example.cryptocurrencytracker.domain.model

data class MarketData(
    val current_price: CurrentPrice,
    val last_updated: String,
    val price_change_percentage_24h: Double
)