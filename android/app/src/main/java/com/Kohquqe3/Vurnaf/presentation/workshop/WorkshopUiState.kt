package com.Kohquqe3.Vurnaf.presentation.workshop

import com.Kohquqe3.Vurnaf.domain.model.ToolId

data class ToolRowUi(
    val id: ToolId,
    val nameRes: Int,
    val effectRes: Int,
    val level: Int,
    val maxLevel: Int,
    val cost: Int,
    val affordable: Boolean,
    val mastered: Boolean
)

data class WorkshopUiState(
    val loaded: Boolean = false,
    val fragments: Int = 0,
    val tools: List<ToolRowUi> = emptyList(),
    val pending: ToolId? = null,
    val pendingCost: Int = 0,
    val messageRes: Int = 0,
    val messageArg: Int = 0
)
