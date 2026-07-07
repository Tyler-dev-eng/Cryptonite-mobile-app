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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tylerdev.cryptonite.domain.model.CoinDomainModel

private val HorizontalPadding = 16.dp
private val VerticalPadding = 12.dp

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
            .padding(horizontal = HorizontalPadding, vertical = VerticalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${coin.rank}. ${coin.name} (${coin.symbol})",
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis
            )

        Text(
            text = if (coin.isActive) "active" else "inactive",
            color = if (coin.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
        )
    }
}
