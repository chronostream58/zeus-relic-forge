package com.Kohquqe3.Vurnaf.presentation.lore

import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.LoreMedallion

data class LoreEntryUi(
    val id: Int,
    val titleRes: Int,
    val bodyRes: Int,
    val medallion: LoreMedallion,
    val gate: LevelId,
    val revealed: Boolean,
    val bonus: Boolean
)

data class LoreUiState(
    val loaded: Boolean = false,
    val entries: List<LoreEntryUi> = emptyList(),
    val revealedCount: Int = 0,
    val total: Int = 0,
    val expandedId: Int = 0
)
