package com.tylerdev.cryptonite.presentation.screens.coin_list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.cryptonite.common.Resource
import com.tylerdev.cryptonite.domain.use_case.get_coins.GetCoinsUseCase
import com.tylerdev.cryptonite.presentation.screens.coin_list.state.CoinListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val state: StateFlow<CoinListUiState> = _state.asStateFlow()

    init {
        fetchCoins()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            getCoinsUseCase().collect { result ->
                _state.value = when (result) {
                    is Resource.Loading -> CoinListUiState.Loading
                    is Resource.Success -> CoinListUiState.Success(result.data ?: emptyList())
                    is Resource.Error -> CoinListUiState.Error(
                        result.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}
