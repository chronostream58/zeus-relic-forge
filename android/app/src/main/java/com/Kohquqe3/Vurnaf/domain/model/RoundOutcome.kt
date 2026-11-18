package com.Kohquqe3.Vurnaf.domain.model

data class RoundOutcome(
    val levelId: LevelId,
    val won: Boolean,
    val moves: Int,
    val par: Int,
    val fragments: Int,
    val resonances: Int,
    val chargeRefunded: Int,
    val challenge: Boolean,
    val sigilEarned: Boolean,
    val nextLevel: LevelId?
)
