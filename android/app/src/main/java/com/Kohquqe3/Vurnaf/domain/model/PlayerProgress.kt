package com.Kohquqe3.Vurnaf.domain.model

import com.Kohquqe3.Vurnaf.core.config.GameConfig

data class PlayerProgress(
    val fragments: Int,
    val crystals: Int,
    val gatesPowered: Int,
    val toolLevels: Map<ToolId, Int>,
    val unlockedLevels: Set<String>,
    val bestMoves: Map<String, Int>,
    val sigils: Set<String>,
    val challengeArmed: Set<String>,
    val unlockedLore: Set<Int>
) {
    fun toolLevel(tool: ToolId): Int = toolLevels[tool] ?: 0

    fun isUnlocked(id: LevelId): Boolean = id == LevelId.FIRST || unlockedLevels.contains(id.key)

    fun isPowered(id: LevelId): Boolean = bestMoves.containsKey(id.key)

    fun hasSigil(id: LevelId): Boolean = sigils.contains(id.key)

    fun isChallengeArmed(id: LevelId): Boolean = challengeArmed.contains(id.key)

    val crystalCap: Int
        get() = GameConfig.CRYSTAL_BASE_CAP + toolLevel(ToolId.CRYSTAL_LATHE).coerceAtMost(2)

    val startingChargeBonus: Int
        get() = toolLevel(ToolId.ROTATION_DIAL) * GameConfig.ROTATION_DIAL_CHARGE_STEP

    fun sigilsInChapter(chapter: Int): Int = sigils.count { it.startsWith("$chapter-") }

    fun firstPlayableLevel(): LevelId {
        for (chapter in 1..GameConfig.CHAPTERS) {
            for (index in 1..GameConfig.LEVELS_PER_CHAPTER) {
                val id = LevelId(chapter, index)
                if (isUnlocked(id) && !isPowered(id)) return id
            }
        }
        return LevelId.FIRST
    }

    companion object {
        val NEW_SAVE = PlayerProgress(
            fragments = 0,
            crystals = 1,
            gatesPowered = 0,
            toolLevels = emptyMap(),
            unlockedLevels = setOf(LevelId.FIRST.key),
            bestMoves = emptyMap(),
            sigils = emptySet(),
            challengeArmed = emptySet(),
            unlockedLore = emptySet()
        )
    }
}
