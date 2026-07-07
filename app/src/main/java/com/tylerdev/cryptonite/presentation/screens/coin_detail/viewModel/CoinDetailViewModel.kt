package com.tylerdev.cryptonite.presentation.screens.coin_detail.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.cryptonite.common.Resource
import com.tylerdev.cryptonite.domain.use_case.get_coin.GetCoinUseCase
import com.tylerdev.cryptonite.presentation.screens.coin_detail.state.CoinDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val COIN_ID_ARG = "coinId"

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val coinId: String = checkNotNull(savedStateHandle[COIN_ID_ARG])

    private val _state = MutableStateFlow(CoinDetailState())
    val state: StateFlow<CoinDetailState> = _state.asStateFlow()

    init {
        fetchCoinDetails()
    }

    private fun fetchCoinDetails() {
        viewModelScope.launch {
            getCoinUseCase(coinId).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value = _state.value.copy(isLoading = true)
                    is Resource.Success -> _state.value = _state.value.copy(
                        isLoading = false,
                        coin = result.data,
                        error = ""
                    )
                    is Resource.Error -> _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }


}