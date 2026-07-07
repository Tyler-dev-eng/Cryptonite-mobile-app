package com.tylerdev.cryptonite.data.remote.mapper

import com.tylerdev.cryptonite.data.remote.dto.CoinDetailDto
import com.tylerdev.cryptonite.data.remote.dto.TeamMember
import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel
import com.tylerdev.cryptonite.domain.model.TeamMemberDomainModel

fun CoinDetailDto.toCoinDetailDomainModel(): CoinDetailDomainModel {
    return CoinDetailDomainModel (
        coinId = id,
        name = name,
        description = description.orEmpty(),
        symbol = symbol,
        rank = rank ?: 0,
        isActive = isActive ?: false,
        tags = tags?.map { it.name } ?: emptyList(),
        team = team?.map { it.toTeamMemberDomainModel() } ?: emptyList()
    )
}

private fun TeamMember.toTeamMemberDomainModel(): TeamMemberDomainModel {
    return TeamMemberDomainModel(
        id = id,
        name = name,
        position = position
    )
}