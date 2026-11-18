package com.Kohquqe3.Vurnaf.domain.repository

import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.LevelSpec
import com.Kohquqe3.Vurnaf.domain.model.LoreEntry
import com.Kohquqe3.Vurnaf.domain.model.ToolDefinition

interface CatalogRepository {
    fun levels(): List<LevelSpec>
    fun level(id: LevelId): LevelSpec
    fun tools(): List<ToolDefinition>
    fun lore(): List<LoreEntry>
}
