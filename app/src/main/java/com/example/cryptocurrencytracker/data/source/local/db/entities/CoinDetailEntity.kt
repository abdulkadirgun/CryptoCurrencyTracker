package com.example.cryptocurrencytracker.data.source.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CoinDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val current_price: Int,
    val last_updated: String,
)