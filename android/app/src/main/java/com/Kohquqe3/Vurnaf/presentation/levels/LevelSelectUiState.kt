package com.Kohquqe3.Vurnaf.presentation.levels

import com.Kohquqe3.Vurnaf.domain.model.LevelId

data class LevelTileUi(
    val id: LevelId,
    val rings: Int,
    val sectors: Int,
    val charge: Int,
    val unlocked: Boolean,
    val powered: Boolean,
    val bestMoves: Int,
    val sigil: Boolean,
    val challengeArmed: Boolean,
    val unlockAfter: LevelId?
)

data class ChapterUi(
    val chapter: Int,
    val titleRes: Int,
    val sigils: Int,
    val levels: List<LevelTileUi>
)

data class LevelSelectUiState(
    val loaded: Boolean = false,
    val chapters: List<ChapterUi> = emptyList(),
    val messageLevel: LevelId? = null
)
