package com.tylerdev.cryptonite.presentation.screens.coin_detail.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tylerdev.cryptonite.common.Resource
import com.tylerdev.cryptonite.domain.use_case.get_coin.GetCoinUseCase
import com.tylerdev.cryptonite.presentation.navigation.Screen
import com.tylerdev.cryptonite.presentation.screens.coin_detail.state.CoinDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val coinId: String = savedStateHandle.toRoute<Screen.CoinDetail>().coinId

    private val _state = MutableStateFlow<CoinDetailUiState>(CoinDetailUiState.Loading)
    val state: StateFlow<CoinDetailUiState> = _state.asStateFlow()

    init {
        fetchCoinDetails()
    }

    private fun fetchCoinDetails() {
        viewModelScope.launch {
            getCoinUseCase(coinId).collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> CoinDetailUiState.Loading
                    is Resource.Success -> CoinDetailUiState.Success(requireNotNull(result.data))
                    is Resource.Error -> CoinDetailUiState.Error(
                        result.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}