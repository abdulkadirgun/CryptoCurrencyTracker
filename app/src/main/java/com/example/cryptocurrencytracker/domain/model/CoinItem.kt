package com.example.cryptocurrencytracker.domain.model

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity

data class CoinItem(
    val id: String = "",
    val name: String = "",
    val symbol: String = "",
    val image: String = "",
    val current_price: Int = 0
){
    fun toEntity(): CoinEntity{
        return CoinEntity(
            id = id,
            name = name,
            symbol = symbol
        )

    }
}