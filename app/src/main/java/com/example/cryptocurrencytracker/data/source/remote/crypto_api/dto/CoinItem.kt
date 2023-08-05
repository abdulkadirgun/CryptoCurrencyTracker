package com.example.cryptocurrencytracker.data.source.remote.crypto_api.dto

import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity

data class CoinItem(
    val id: String,
    val name: String,
    val symbol: String
){
    fun toEntity(): CoinEntity{
        return CoinEntity(
            id = id,
            name = name,
            symbol = symbol
        )

    }
}