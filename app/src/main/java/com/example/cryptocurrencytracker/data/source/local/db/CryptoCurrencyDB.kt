package com.example.cryptocurrencytracker.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinDetailEntity
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity

/** sadece CoinDetailEntity yeterliydi. ancak veritabanına kayıt işleminin hızlı olması için 2 entity kullanıldı
 **/
@Database(entities = [CoinEntity::class, CoinDetailEntity::class], version = 1, exportSchema = false)

abstract class CryptoCurrencyDB : RoomDatabase(){
    abstract fun getDao(): CryptoCurrencyDao

}