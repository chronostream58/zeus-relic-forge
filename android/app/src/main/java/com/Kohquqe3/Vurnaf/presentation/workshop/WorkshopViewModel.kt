package com.Kohquqe3.Vurnaf.presentation.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.model.ToolId
import com.Kohquqe3.Vurnaf.domain.repository.CatalogRepository
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository
import com.Kohquqe3.Vurnaf.domain.usecase.ForgeToolUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkshopViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository,
    private val forgeTool: ForgeToolUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkshopUiState())
    val state: StateFlow<WorkshopUiState> = _state.asStateFlow()

    private var progress: PlayerProgress = PlayerProgress.NEW_SAVE

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            progress = progressRepository.load()
            _state.update {
                it.copy(
                    loaded = true,
                    fragments = progress.fragments,
                    tools = buildTools(),
                    pending = null,
                    messageRes = 0,
                    messageArg = 0
                )
            }
        }
    }

    fun onForgeRequested(tool: ToolId) {
        val level = progress.toolLevel(tool)
        if (level >= GameConfig.TOOL_MAX_LEVEL) return
        val price = forgeTool.priceOf(level)
        if (progress.fragments < price) {
            _state.update { it.copy(messageRes = R.string.workshop_need, messageArg = price) }
            return
        }
        _state.update { it.copy(pending = tool, pendingCost = price, messageRes = 0, messageArg = 0) }
    }

    fun onDismiss() {
        _state.update { it.copy(pending = null) }
    }

    fun onConfirm() {
        val tool = _state.value.pending ?: return
        val updated = forgeTool(progress, tool)
        if (updated == null) {
            _state.update { it.copy(pending = null) }
            return
        }
        progress = updated
        progressRepository.save(progress)
        _state.update {
            it.copy(
                fragments = progress.fragments,
                tools = buildTools(),
                pending = null,
                messageRes = R.string.workshop_forged,
                messageArg = 0
            )
        }
    }

    private fun buildTools(): List<ToolRowUi> = catalogRepository.tools().map { definition ->
        val level = progress.toolLevel(definition.id)
        val mastered = level >= GameConfig.TOOL_MAX_LEVEL
        val price = if (mastered) 0 else forgeTool.priceOf(level)
        ToolRowUi(
            id = definition.id,
            nameRes = definition.nameRes,
            effectRes = definition.effectRes[level.coerceIn(0, definition.effectRes.lastIndex)],
            level = level,
            maxLevel = GameConfig.TOOL_MAX_LEVEL,
            cost = price,
            affordable = !mastered && progress.fragments >= price,
            mastered = mastered
        )
    }
}
