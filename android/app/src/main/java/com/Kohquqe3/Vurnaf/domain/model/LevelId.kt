package com.Kohquqe3.Vurnaf.domain.model

import com.Kohquqe3.Vurnaf.core.config.GameConfig

data class LevelId(val chapter: Int, val index: Int) {

    val key: String get() = "$chapter-$index"

    val ordinal: Int get() = (chapter - 1) * GameConfig.LEVELS_PER_CHAPTER + index

    fun next(): LevelId? {
        if (index < GameConfig.LEVELS_PER_CHAPTER) return LevelId(chapter, index + 1)
        if (chapter < GameConfig.CHAPTERS) return LevelId(chapter + 1, 1)
        return null
    }

    fun previous(): LevelId? {
        if (index > 1) return LevelId(chapter, index - 1)
        if (chapter > 1) return LevelId(chapter - 1, GameConfig.LEVELS_PER_CHAPTER)
        return null
    }

    companion object {
        val FIRST = LevelId(1, 1)
    }
}
