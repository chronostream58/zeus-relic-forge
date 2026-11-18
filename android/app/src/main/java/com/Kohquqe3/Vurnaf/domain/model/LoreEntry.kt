package com.Kohquqe3.Vurnaf.domain.model

data class LoreEntry(
    val id: Int,
    val titleRes: Int,
    val bodyRes: Int,
    val medallion: LoreMedallion,
    val gate: LevelId,
    val bonus: Boolean
)
