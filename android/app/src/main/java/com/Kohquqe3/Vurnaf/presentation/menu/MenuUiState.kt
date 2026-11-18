package com.Kohquqe3.Vurnaf.presentation.menu

import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.model.LevelId

data class MenuUiState(
    val loaded: Boolean = false,
    val gatesPowered: Int = 0,
    val totalGates: Int = GameConfig.TOTAL_LEVELS,
    val fragments: Int = 0,
    val crystals: Int = 0,
    val nextLevel: LevelId = LevelId.FIRST,
    val challengeArmed: Boolean = false
)
