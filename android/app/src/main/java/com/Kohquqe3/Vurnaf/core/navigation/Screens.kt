package com.Kohquqe3.Vurnaf.core.navigation

import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.RoundOutcome

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data class Game(val levelId: LevelId, val challenge: Boolean, val attempt: Int) : Screen

    data class GameOver(val outcome: RoundOutcome) : Screen

    data object LevelSelect : Screen

    data object Workshop : Screen

    data object Lore : Screen
}
