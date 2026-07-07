package com.tylerdev.cryptonite.presentation.screens.coin_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.cryptonite.presentation.screens.coin_detail.components.CoinTag
import com.tylerdev.cryptonite.presentation.screens.coin_detail.components.TeamListItem
import com.tylerdev.cryptonite.presentation.screens.coin_detail.state.CoinDetailUiState
import com.tylerdev.cryptonite.presentation.screens.coin_detail.viewModel.CoinDetailViewModel

private val ScreenPadding = 20.dp
private val SectionSpacing = 15.dp
private val TagSpacing = 10.dp
private val TeamListItemPadding = 10.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val currentState = state) {
            is CoinDetailUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is CoinDetailUiState.Error -> Text(
                text = currentState.message,
                modifier = Modifier.align(Alignment.Center)
            )

            is CoinDetailUiState.Success -> {
                val coin = currentState.coin
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(ScreenPadding)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${coin.rank}. ${coin.name} (${coin.symbol})",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.weight(8f)

                            )
                            Text(
                                text = if (coin.isActive) "active" else "inactive",
                                color = if (coin.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        Text(
                            text = coin.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(TagSpacing),
                            verticalArrangement = Arrangement.spacedBy(TagSpacing)
                        ) {
                            coin.tags.forEach { tag ->
                                CoinTag(tag = tag)
                            }
                        }
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        if (coin.team.isNotEmpty()) {
                            Text(
                                text = "Team members",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(SectionSpacing))
                            coin.team.forEach { teamMember ->
                                TeamListItem(
                                    teamMember = teamMember,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(TeamListItemPadding)
                                )
                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
