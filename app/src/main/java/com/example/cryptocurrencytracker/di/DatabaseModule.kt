package com.example.cryptocurrencytracker.di

import android.content.Context
import androidx.room.Room
import com.example.cryptocurrencytracker.data.source.local.db.CryptoCurrencyDB
import com.example.cryptocurrencytracker.data.source.local.db.RoomDataSourceImpl
import com.example.cryptocurrencytracker.domain.sources.RoomDataSource
import com.example.cryptocurrencytracker.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : CryptoCurrencyDB {
        return Room.databaseBuilder(context, CryptoCurrencyDB::class.java, Constants.CRYPTO_CURRENCY_DATABASE).build()
    }

}