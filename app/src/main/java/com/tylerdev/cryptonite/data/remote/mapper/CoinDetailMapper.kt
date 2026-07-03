package com.tylerdev.cryptonite.data.remote.mapper

import com.tylerdev.cryptonite.data.remote.dto.CoinDetailDto
import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel

fun CoinDetailDto.toCoinDetailDomainModel(): CoinDetailDomainModel {
    return CoinDetailDomainModel (
        coinId = id,
        name = name,
        description = description,
        symbol = symbol,
        rank = rank,
        isActive = isActive,
        tags = tags.map { it.name },
        team = team
    )
}