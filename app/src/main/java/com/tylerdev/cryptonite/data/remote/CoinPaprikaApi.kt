package com.tylerdev.cryptonite.data.remote

import com.tylerdev.cryptonite.data.remote.dto.CoinDetailDto
import com.tylerdev.cryptonite.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinPaprikaApi {
    companion object {
        const val BASE_URL = "https://api.coinpaprika.com/"
    }

    @GET("v1/coins")
    suspend fun getCoins(): List<CoinDto>

    @GET("v1/coins/{coinId}")
    suspend fun getCoinById(@Path("coinId") coinId: String): CoinDetailDto

}