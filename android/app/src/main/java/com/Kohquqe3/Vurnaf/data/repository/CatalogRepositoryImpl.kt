package com.Kohquqe3.Vurnaf.data.repository

import com.Kohquqe3.Vurnaf.data.sample.SampleData
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.LevelSpec
import com.Kohquqe3.Vurnaf.domain.model.LoreEntry
import com.Kohquqe3.Vurnaf.domain.model.ToolDefinition
import com.Kohquqe3.Vurnaf.domain.repository.CatalogRepository

class CatalogRepositoryImpl : CatalogRepository {

    override fun levels(): List<LevelSpec> = SampleData.levels

    override fun level(id: LevelId): LevelSpec =
        SampleData.levels.firstOrNull { it.id == id } ?: SampleData.levels.first()

    override fun tools(): List<ToolDefinition> = SampleData.tools

    override fun lore(): List<LoreEntry> = SampleData.lore
}
