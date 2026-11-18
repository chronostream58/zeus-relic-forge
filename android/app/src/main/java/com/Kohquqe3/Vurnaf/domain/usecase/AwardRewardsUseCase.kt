package com.Kohquqe3.Vurnaf.domain.usecase

import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.model.RoundOutcome
import com.Kohquqe3.Vurnaf.domain.model.ToolId

class AwardRewardsUseCase {

    operator fun invoke(progress: PlayerProgress, outcome: RoundOutcome): PlayerProgress {
        val banked = progress.copy(fragments = progress.fragments + outcome.fragments)
        if (!outcome.won) return banked

        val key = outcome.levelId.key
        val firstClear = !progress.bestMoves.containsKey(key)
        val gates = if (firstClear) progress.gatesPowered + 1 else progress.gatesPowered
        val previousBest = progress.bestMoves[key]
        val bestMoves = if (previousBest == null || outcome.moves < previousBest) {
            progress.bestMoves + (key to outcome.moves)
        } else {
            progress.bestMoves
        }
        val sigils = if (outcome.sigilEarned) progress.sigils + key else progress.sigils
        val unlocked = progress.unlockedLevels + key + listOfNotNull(outcome.nextLevel?.key)

        var lore = progress.unlockedLore + outcome.levelId.ordinal
        val chapterSigils = sigils.count { it.startsWith("${outcome.levelId.chapter}-") }
        if (chapterSigils >= GameConfig.SIGILS_FOR_BONUS_LORE) {
            lore = lore + (GameConfig.TOTAL_LEVELS + outcome.levelId.chapter)
        }

        val lathe = progress.toolLevel(ToolId.CRYSTAL_LATHE)
        val interval = if (lathe >= GameConfig.TOOL_MAX_LEVEL) {
            GameConfig.CRYSTAL_FAST_INTERVAL
        } else {
            GameConfig.CRYSTAL_BASE_INTERVAL
        }
        val crystals = if (firstClear && gates % interval == 0) {
            (progress.crystals + 1).coerceAtMost(progress.crystalCap)
        } else {
            progress.crystals
        }

        return banked.copy(
            crystals = crystals,
            gatesPowered = gates,
            unlockedLevels = unlocked,
            bestMoves = bestMoves,
            sigils = sigils,
            unlockedLore = lore
        )
    }
}
