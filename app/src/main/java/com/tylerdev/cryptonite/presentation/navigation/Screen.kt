package com.tylerdev.cryptonite.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object CoinList : Screen

    @Serializable
    data class CoinDetail(val coinId: String) : Screen
}
