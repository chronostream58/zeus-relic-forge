package com.Kohquqe3.Vurnaf.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val CHAPTERS = 3
    const val LEVELS_PER_CHAPTER = 5
    const val TOTAL_LEVELS = CHAPTERS * LEVELS_PER_CHAPTER
    const val MAX_RINGS = 4

    const val ROTATION_COST = 1
    const val ANCHORED_COST = 2
    const val SURGE_BACKLASH_COST = 3

    const val GATE_FRAGMENTS = 2
    const val RESONANCE_FRAGMENTS = 1
    const val RESONANCE_CHARGE = 1
    const val CHALLENGE_FRAGMENTS = 3

    const val CRYSTAL_BASE_CAP = 3
    const val CRYSTAL_BASE_INTERVAL = 3
    const val CRYSTAL_FAST_INTERVAL = 2
    const val SIGILS_FOR_BONUS_LORE = 3

    const val TOOL_MAX_LEVEL = 3
    const val ROTATION_DIAL_CHARGE_STEP = 2

    val TOOL_COSTS = listOf(6, 12, 20)

    const val SEED_PRIME_A = 7919L
    const val SEED_PRIME_B = 104729L
    const val GENERATION_ATTEMPTS = 48

    const val ROTATION_ANIM_MS = 220
    const val LOCK_FLASH_MS = 180L
    const val BACKLASH_MS = 260L
    const val WIN_FLARE_MS = 520L
    const val COLLAPSE_MS = 320L
    const val MESSAGE_MS = 2000L
}
