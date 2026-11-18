package com.Kohquqe3.Vurnaf.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val loaded: Boolean = false,
    val fragments: Int = 0,
    val crystals: Int = 0,
    val gatesPowered: Int = 0,
    val totalGates: Int = GameConfig.TOTAL_LEVELS
)

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    loaded = true,
                    fragments = progress.fragments,
                    crystals = progress.crystals,
                    gatesPowered = progress.gatesPowered
                )
            }
        }
    }
}
