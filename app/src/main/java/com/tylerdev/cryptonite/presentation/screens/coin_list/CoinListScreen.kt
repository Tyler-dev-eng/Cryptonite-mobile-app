package com.tylerdev.cryptonite.presentation.screens.coin_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import  androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tylerdev.cryptonite.domain.model.CoinDomainModel
import com.tylerdev.cryptonite.presentation.screens.coin_list.components.CoinListItem
import com.tylerdev.cryptonite.presentation.screens.coin_list.state.CoinListUiState
import com.tylerdev.cryptonite.presentation.screens.coin_list.viewModel.CoinListViewModel

@Composable
fun CoinListScreen(
    onCoinClick: (CoinDomainModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val currentState = state) {
            is CoinListUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is CoinListUiState.Error -> Text(
                text = currentState.message,
                modifier = Modifier.align(Alignment.Center)
            )
            is CoinListUiState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(currentState.coins, key = { it.id }) { coin ->
                    CoinListItem(coin = coin, onClick = onCoinClick)
                }
            }
        }
    }
}
