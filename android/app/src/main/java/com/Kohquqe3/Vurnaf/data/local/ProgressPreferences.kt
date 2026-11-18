package com.Kohquqe3.Vurnaf.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.model.ToolId

class ProgressPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun read(): PlayerProgress {
        if (!prefs.contains(KEY_GATES)) return PlayerProgress.NEW_SAVE
        val tools = ToolId.entries.associateWith { prefs.getInt(toolKey(it), 0) }
        return PlayerProgress(
            fragments = prefs.getInt(KEY_FRAGMENTS, 0),
            crystals = prefs.getInt(KEY_CRYSTALS, 1),
            gatesPowered = prefs.getInt(KEY_GATES, 0),
            toolLevels = tools,
            unlockedLevels = readSet(KEY_UNLOCKED, LevelId.FIRST.key),
            bestMoves = readBestMoves(),
            sigils = readSet(KEY_SIGILS, ""),
            challengeArmed = readSet(KEY_CHALLENGE, ""),
            unlockedLore = readSet(KEY_LORE, "").mapNotNull { it.toIntOrNull() }.toSet()
        )
    }

    fun write(progress: PlayerProgress) {
        val editor = prefs.edit()
        editor.putInt(KEY_FRAGMENTS, progress.fragments)
        editor.putInt(KEY_CRYSTALS, progress.crystals)
        editor.putInt(KEY_GATES, progress.gatesPowered)
        ToolId.entries.forEach { editor.putInt(toolKey(it), progress.toolLevel(it)) }
        editor.putString(KEY_UNLOCKED, progress.unlockedLevels.joinToString(SEPARATOR))
        editor.putString(KEY_SIGILS, progress.sigils.joinToString(SEPARATOR))
        editor.putString(KEY_CHALLENGE, progress.challengeArmed.joinToString(SEPARATOR))
        editor.putString(KEY_LORE, progress.unlockedLore.joinToString(SEPARATOR))
        editor.putString(
            KEY_BEST,
            progress.bestMoves.entries.joinToString(SEPARATOR) { "${it.key}=${it.value}" }
        )
        editor.apply()
    }

    private fun readSet(key: String, fallback: String): Set<String> {
        val raw = prefs.getString(key, fallback).orEmpty()
        if (raw.isBlank()) return emptySet()
        return raw.split(SEPARATOR).filter { it.isNotBlank() }.toSet()
    }

    private fun readBestMoves(): Map<String, Int> {
        val raw = prefs.getString(KEY_BEST, "").orEmpty()
        if (raw.isBlank()) return emptyMap()
        return raw.split(SEPARATOR).mapNotNull { chunk ->
            val parts = chunk.split("=")
            val moves = parts.getOrNull(1)?.toIntOrNull()
            if (parts.size == 2 && moves != null) parts[0] to moves else null
        }.toMap()
    }

    private fun toolKey(tool: ToolId): String = "tool_level_" + tool.name.lowercase()

    private companion object {
        const val STORE = "zeus_relic_forge_progress"
        const val SEPARATOR = ","
        const val KEY_FRAGMENTS = "fragments"
        const val KEY_CRYSTALS = "crystals"
        const val KEY_GATES = "gates_powered"
        const val KEY_UNLOCKED = "unlocked_levels"
        const val KEY_BEST = "best_moves"
        const val KEY_SIGILS = "sigils"
        const val KEY_CHALLENGE = "challenge_armed"
        const val KEY_LORE = "unlocked_lore"
    }
}
