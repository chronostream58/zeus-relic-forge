package com.Kohquqe3.Vurnaf.domain.usecase

import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.model.ToolId

class ForgeToolUseCase {

    fun priceOf(level: Int): Int = GameConfig.TOOL_COSTS.getOrElse(level) { Int.MAX_VALUE }

    operator fun invoke(progress: PlayerProgress, tool: ToolId): PlayerProgress? {
        val level = progress.toolLevel(tool)
        if (level >= GameConfig.TOOL_MAX_LEVEL) return null
        val price = priceOf(level)
        if (progress.fragments < price) return null
        return progress.copy(
            fragments = progress.fragments - price,
            toolLevels = progress.toolLevels + (tool to level + 1)
        )
    }
}
