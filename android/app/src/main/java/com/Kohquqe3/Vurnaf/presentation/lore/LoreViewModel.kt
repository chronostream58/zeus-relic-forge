package com.Kohquqe3.Vurnaf.presentation.lore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kohquqe3.Vurnaf.domain.repository.CatalogRepository
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoreViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoreUiState())
    val state: StateFlow<LoreUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val entries = catalogRepository.lore().map { entry ->
                LoreEntryUi(
                    id = entry.id,
                    titleRes = entry.titleRes,
                    bodyRes = entry.bodyRes,
                    medallion = entry.medallion,
                    gate = entry.gate,
                    revealed = progress.unlockedLore.contains(entry.id),
                    bonus = entry.bonus
                )
            }
            _state.update {
                it.copy(
                    loaded = true,
                    entries = entries,
                    revealedCount = entries.count { entry -> entry.revealed },
                    total = entries.size
                )
            }
        }
    }

    fun toggleExpanded(id: Int) {
        _state.update { it.copy(expandedId = if (it.expandedId == id) 0 else id) }
    }
}
