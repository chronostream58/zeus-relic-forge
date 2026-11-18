package com.Kohquqe3.Vurnaf.domain.model

data class RotationResult(
    val board: BoardState,
    val anchored: Boolean,
    val lockedNow: Boolean,
    val chargeCost: Int
)
