package com.example.cryptocurrencytracker.data.source.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CoinEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String
    )