package com.Kohquqe3.Vurnaf.domain.model

data class LevelSpec(
    val id: LevelId,
    val rings: Int,
    val sectors: Int,
    val charge: Int,
    val parMin: Int,
    val parMax: Int,
    val gateNodes: Int,
    val decoys: Boolean
)
