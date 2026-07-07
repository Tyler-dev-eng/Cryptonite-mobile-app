package com.tylerdev.cryptonite.domain.use_case.get_coins

import com.tylerdev.cryptonite.common.Resource
import com.tylerdev.cryptonite.data.remote.mapper.toCoinDomainModel
import com.tylerdev.cryptonite.domain.model.CoinDomainModel
import com.tylerdev.cryptonite.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<Resource<List<CoinDomainModel>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getCoins().map { it.toCoinDomainModel() }
            emit(Resource.Success(coins))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (_: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}