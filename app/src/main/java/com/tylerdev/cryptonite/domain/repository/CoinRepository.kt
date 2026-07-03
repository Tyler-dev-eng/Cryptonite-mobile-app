package com.tylerdev.cryptonite.domain.repository

import com.tylerdev.cryptonite.data.remote.dto.CoinDetailDto
import com.tylerdev.cryptonite.data.remote.dto.CoinDto

interface CoinRepository {

    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}