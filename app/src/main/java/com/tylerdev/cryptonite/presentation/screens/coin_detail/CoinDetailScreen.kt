package com.tylerdev.cryptonite.presentation.screens.coin_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.tylerdev.cryptonite.data.remote.dto.TeamMember
import com.tylerdev.cryptonite.domain.model.CoinDetailDomainModel
import com.tylerdev.cryptonite.presentation.screens.coin_detail.components.CoinTag
import com.tylerdev.cryptonite.presentation.screens.coin_detail.components.TeamListItem
import com.tylerdev.cryptonite.presentation.screens.coin_detail.state.CoinDetailUiState
import com.tylerdev.cryptonite.presentation.screens.coin_detail.viewModel.CoinDetailViewModel

private val ScreenPadding = 20.dp
private val SectionSpacing = 15.dp
private val TagSpacing = 10.dp
private val TeamListItemPadding = 10.dp
private const val CoinNameRowWeight = 8f

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
                        CoinHeader(coin)
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        Text(
                            text = coin.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        CoinTagsSection(coin.tags)
                        Spacer(modifier = Modifier.height(SectionSpacing))
                        if (coin.team.isNotEmpty()) {
                            TeamSection(coin.team)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinHeader(coin: CoinDetailDomainModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${coin.rank}. ${coin.name} (${coin.symbol})",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.weight(CoinNameRowWeight)
        )
        Text(
            text = if (coin.isActive) "active" else "inactive",
            color = if (coin.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoinTagsSection(tags: List<String>) {
    Text(
        text = "Tags",
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(SectionSpacing))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(TagSpacing),
        verticalArrangement = Arrangement.spacedBy(TagSpacing)
    ) {
        tags.forEach { tag ->
            CoinTag(tag = tag)
        }
    }
}

@Composable
private fun TeamSection(team: List<TeamMember>) {
    Text(
        text = "Team members",
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(SectionSpacing))
    team.forEachIndexed { index, teamMember ->
        TeamListItem(
            teamMember = teamMember,
            modifier = Modifier
                .fillMaxWidth()
                .padding(TeamListItemPadding)
        )
        if (index != team.lastIndex) {
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                DividerDefaults.color
            )
        }
    }
}
