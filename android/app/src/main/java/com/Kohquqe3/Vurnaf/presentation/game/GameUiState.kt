package com.Kohquqe3.Vurnaf.presentation.game

import com.Kohquqe3.Vurnaf.domain.model.BoardState
import com.Kohquqe3.Vurnaf.domain.model.LevelId

data class GameUiState(
    val ready: Boolean = false,
    val levelId: LevelId = LevelId.FIRST,
    val chapterTitleRes: Int = 0,
    val board: BoardState? = null,
    val charge: Int = 0,
    val maxCharge: Int = 0,
    val moves: Int = 0,
    val par: Int = 0,
    val crystals: Int = 0,
    val challenge: Boolean = false,
    val gateNodes: Int = 1,
    val previewLevel: Int = 0,
    val busyRing: Int = -1,
    val flashRing: Int = -1,
    val shake: Boolean = false,
    val backlash: Boolean = false,
    val winFlare: Boolean = false,
    val resonances: Int = 0,
    val messageRes: Int = 0,
    val messageArgs: List<Int> = emptyList(),
    val paused: Boolean = false,
    val finished: Boolean = false
) {
    val armed: Boolean get() = board?.solved == true
}
