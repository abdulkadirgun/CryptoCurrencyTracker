package com.example.cryptocurrencytracker.domain.model

data class CoinDetailItem(
    val hashing_algorithm: String,
    val id: String,
    val description : Description,
    val market_data: MarketData,
    val name: String,
    val symbol: String,
    val image: Image
){
    fun toCoinItem(): CoinItem {
        return CoinItem(
            id = id,
            name = name,
            symbol= symbol,
            image = image.large,
            current_price = market_data.current_price.usd
        )

    }
}