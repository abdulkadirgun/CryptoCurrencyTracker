package com.example.cryptocurrencytracker.data.source.remote.crypto_api

import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoApi {

    @GET("coins/list")
    suspend fun getCoinList() : Response<List<CoinItem>>

    @GET("coins/{id}")
    suspend fun getCoinById(
        @Path("id") coinId :String
    ) : Response<CoinDetailItem>



}