package com.tylerdev.cryptonite.presentation.screens.coin_detail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val BorderWidth = 1.dp
private val CornerRadius = 100.dp
private val TagPadding = 10.dp

@Composable
fun CoinTag(
    tag: String
) {
    Box(
        modifier = Modifier
            .border(
                width = BorderWidth,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(CornerRadius)
            )
            .padding(TagPadding)
    ) {
        Text(
            text = tag,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}