package com.tylerdev.cryptonite.domain.model

data class CoinDomainModel (
    val id: String,
    val isActive: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
)