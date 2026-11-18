package com.Kohquqe3.Vurnaf.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val next = progress.firstPlayableLevel()
            _state.update {
                it.copy(
                    loaded = true,
                    gatesPowered = progress.gatesPowered,
                    fragments = progress.fragments,
                    crystals = progress.crystals,
                    nextLevel = next,
                    challengeArmed = progress.isChallengeArmed(next)
                )
            }
        }
    }
}
