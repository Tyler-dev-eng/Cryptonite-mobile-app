package com.tylerdev.cryptonite.domain.repository

import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel
import com.tylerdev.cryptonite.domain.model.CoinDomainModel

interface CoinRepository {

    suspend fun getCoins(): List<CoinDomainModel>

    suspend fun getCoinById(coinId: String): CoinDetailDomainModel
}