package com.tylerdev.cryptonite.data.repository

import com.tylerdev.cryptonite.data.remote.CoinPaprikaApi
import com.tylerdev.cryptonite.data.remote.mapper.toCoinDetailDomainModel
import com.tylerdev.cryptonite.data.remote.mapper.toCoinDomainModel
import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel
import com.tylerdev.cryptonite.domain.model.CoinDomainModel
import com.tylerdev.cryptonite.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
): CoinRepository {
    override suspend fun getCoins(): List<CoinDomainModel> {
        return api.getCoins().map { it.toCoinDomainModel() }
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDomainModel {
        return api.getCoinById(coinId).toCoinDetailDomainModel()
    }
}