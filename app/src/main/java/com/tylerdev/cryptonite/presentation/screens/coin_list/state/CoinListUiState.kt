package com.tylerdev.cryptonite.presentation.screens.coin_list.state

import com.tylerdev.cryptonite.domain.model.CoinDomainModel

sealed class CoinListUiState {
    data object Loading : CoinListUiState()
    data class Success(val coins: List<CoinDomainModel>) : CoinListUiState()
    data class Error(val message: String) : CoinListUiState()
}
