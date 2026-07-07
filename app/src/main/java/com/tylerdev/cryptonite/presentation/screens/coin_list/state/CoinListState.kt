package com.tylerdev.cryptonite.presentation.screens.coin_list.state

import com.tylerdev.cryptonite.domain.model.CoinDomainModel

data class CoinListState (
    val isLoading: Boolean = false,
    val coins: List<CoinDomainModel> = emptyList(),
    val error: String = ""
)