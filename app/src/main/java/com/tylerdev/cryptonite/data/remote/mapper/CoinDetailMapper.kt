package com.tylerdev.cryptonite.data.remote.mapper

import com.tylerdev.cryptonite.data.remote.dto.CoinDetailDto
import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel

fun CoinDetailDto.toCoinDetailDomainModel(): CoinDetailDomainModel {
    return CoinDetailDomainModel (
        coinId = id,
        name = name,
        description = description.orEmpty(),
        symbol = symbol,
        rank = rank ?: 0,
        isActive = isActive ?: false,
        tags = tags?.map { it.name } ?: emptyList(),
        team = team ?: emptyList()
    )
}