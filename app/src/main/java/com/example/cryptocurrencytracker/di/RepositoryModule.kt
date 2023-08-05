package com.example.cryptocurrencytracker.di

import com.example.cryptocurrencytracker.data.repository.CryptoCurrencyRepositoryImpl
import com.example.cryptocurrencytracker.data.source.local.db.CryptoCurrencyDB
import com.example.cryptocurrencytracker.data.source.local.db.RoomDataSourceImpl
import com.example.cryptocurrencytracker.data.source.remote.authentication.AuthDataSourceImpl
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.CryptoApi
import com.example.cryptocurrencytracker.data.source.remote.crypto_api.CryptoApiDataSourceImpl
import com.example.cryptocurrencytracker.data.source.remote.firestore.FirestoreDataSourceImpl
import com.example.cryptocurrencytracker.domain.repository.CryptoCurrencyRepository
import com.example.cryptocurrencytracker.domain.sources.AuthDataSource
import com.example.cryptocurrencytracker.domain.sources.CryptoApiDataSource
import com.example.cryptocurrencytracker.domain.sources.FirestoreDataSource
import com.example.cryptocurrencytracker.domain.sources.RoomDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRoomDataSource(cryptoCurrencyDB: CryptoCurrencyDB): RoomDataSource {
        return RoomDataSourceImpl(cryptoCurrencyDB)
    }

    @Provides
    @Singleton
    fun provideCryptoApiDataSource(cryptoApi: CryptoApi) : CryptoApiDataSource = CryptoApiDataSourceImpl(cryptoApi)

    @Provides
    @Singleton
    fun provideAuthDataSource(auth: FirebaseAuth) : AuthDataSource = AuthDataSourceImpl(auth)

    @Provides
    @Singleton
    fun provideFirestoreDataSource(firebaseFirestore: FirebaseFirestore) : FirestoreDataSource = FirestoreDataSourceImpl(firebaseFirestore)


    @Provides
    @Singleton
    fun provideCryptoCurrencyRepository(
        roomDataSource: RoomDataSource,
        cryptoApiDataSource: CryptoApiDataSource,
        authDataSource: AuthDataSource,
        firestoreDataSource: FirestoreDataSource
    ) : CryptoCurrencyRepository {
        return CryptoCurrencyRepositoryImpl(roomDataSource, cryptoApiDataSource, authDataSource,firestoreDataSource)
    }


}