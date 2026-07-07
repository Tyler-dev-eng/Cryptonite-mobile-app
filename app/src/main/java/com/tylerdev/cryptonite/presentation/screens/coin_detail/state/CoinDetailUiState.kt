package com.tylerdev.cryptonite.presentation.screens.coin_detail.state

import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel

sealed class CoinDetailUiState {
    data object Loading : CoinDetailUiState()
    data class Success(val coin: CoinDetailDomainModel) : CoinDetailUiState()
    data class Error(val message: String) : CoinDetailUiState()
}
