package com.tylerdev.cryptonite.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tylerdev.cryptonite.presentation.screens.coin_detail.CoinDetailScreen
import com.tylerdev.cryptonite.presentation.screens.coin_list.CoinListScreen

@Composable
fun CryptoniteNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CoinList,
        modifier = modifier
    ) {
        composable<Screen.CoinList> {
            CoinListScreen(
                onCoinClick = { coin ->
                    navController.navigate(Screen.CoinDetail(coinId = coin.id))
                }
            )
        }
        composable<Screen.CoinDetail> {
            CoinDetailScreen()
        }
    }
}
