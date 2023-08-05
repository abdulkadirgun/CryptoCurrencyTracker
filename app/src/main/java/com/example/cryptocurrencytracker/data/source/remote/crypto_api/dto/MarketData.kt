package com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto

data class MarketData(
    val current_price: CurrentPrice,
    val last_updated: String,
    val price_change_percentage_24h: Double
)