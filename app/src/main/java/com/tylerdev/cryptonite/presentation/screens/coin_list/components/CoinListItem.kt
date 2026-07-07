package com.tylerdev.cryptonite.presentation.screens.coin_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tylerdev.cryptonite.domain.model.CoinDomainModel

@Composable
fun CoinListItem(
    coin: CoinDomainModel,
    onClick: (CoinDomainModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(coin) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = coin.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = coin.symbol, style = MaterialTheme.typography.bodyMedium)
    }
}
