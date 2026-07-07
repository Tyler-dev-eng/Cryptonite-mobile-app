package com.tylerdev.cryptonite.domain.use_case.get_coin

import com.tylerdev.cryptonite.common.Resource
import com.tylerdev.cryptonite.data.remote.mapper.toCoinDetailDomainModel
import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel
import com.tylerdev.cryptonite.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCoinUserCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetailDomainModel>> = flow {
        try {
            emit(Resource.Loading())
            val coin = repository.getCoinById(coinId).toCoinDetailDomainModel()
            emit(Resource.Success(coin))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (_: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}