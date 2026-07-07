package com.tylerdev.cryptonite.presentation.screens.coin_detail.state

import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel

data class CoinDetailState (
    val isLoading: Boolean = false,
    val coin: CoinDetailDomainModel? = null,
    val error: String = ""
)