package com.tylerdev.cryptonite.data.repository

import com.tylerdev.cryptonite.data.remote.CoinPaprikaApi
import com.tylerdev.cryptonite.data.remote.dto.CoinDetailDto
import com.tylerdev.cryptonite.data.remote.dto.CoinDto
import com.tylerdev.cryptonite.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
): CoinRepository {
    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}