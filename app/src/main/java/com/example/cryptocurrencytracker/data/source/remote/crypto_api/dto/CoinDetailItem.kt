package com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto

data class CoinDetailItem(
    val hashing_algorithm: String,
    val id: String,
    val description : Description,
    val market_data: MarketData,
    val name: String,
    val symbol: String,
    val image: Image
)