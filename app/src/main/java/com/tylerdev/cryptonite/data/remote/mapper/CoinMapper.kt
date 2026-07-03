package com.tylerdev.cryptonite.data.remote.mapper

import com.tylerdev.cryptonite.data.remote.dto.CoinDto
import com.tylerdev.cryptonite.domain.model.CoinDomainModel

fun CoinDto.toCoinDomainModel(): CoinDomainModel {
    return CoinDomainModel(
        id = id,
        isActive = isActive,
        name = name,
        rank = rank,
        symbol = symbol,
    )
}