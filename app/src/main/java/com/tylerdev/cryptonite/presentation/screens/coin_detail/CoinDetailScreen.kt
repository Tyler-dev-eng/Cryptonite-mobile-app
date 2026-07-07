package com.tylerdev.cryptonite.presentation.screens.coin_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.cryptonite.presentation.screens.coin_detail.viewModel.CoinDetailViewModel

@Composable
fun CoinDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.error.isNotBlank() -> Text(
                text = state.error,
                modifier = Modifier.align(Alignment.Center)
            )
            state.coin != null -> {
                val coin = state.coin!!
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = coin.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = coin.symbol, style = MaterialTheme.typography.titleMedium)
                    Text(text = coin.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
