package com.example.cryptocurrencytracker.domain.usecases.home

import android.util.Log
import com.example.cryptocurrencytracker.util.Constants
import com.example.cryptocurrencytracker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCoinsForEachSessionUseCase @Inject constructor(
    private val getCoinsFromApiAndSaveDBUseCase: GetCoinsFromApiAndSaveDBUseCase,
    private val getCoinsFromDBUseCase: GetCoinsFromDBUseCase
) {

    suspend operator fun invoke(): Flow<Resource<Any>> {
        return  flow {

            val resultOfSave = getCoinsFromApiAndSaveDBUseCase()

            resultOfSave.collect{ result->
                when(result){
                    is Resource.Error -> {
                        Log.d("HomeViewModel", "Error: ")
                        emit(Resource.Error(result.message))
                    }
                    is Resource.Loading -> {
                        Log.d("HomeViewModel", "Loading: ")
                        emit(Resource.Loading())
                    }
                    is Resource.Success -> {
                        Constants.INITIAL_FETCH = true
                        Log.d("HomeViewModel", "Success: ")
                            val coinsFromDB = getCoinsFromDBUseCase()
                            coinsFromDB.collect{ param->
                                emit(Resource.Success(param))
                            }
                    }
                }
            }
        }
    }
}