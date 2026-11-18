package com.Kohquqe3.Vurnaf.domain.model

data class BoardState(
    val rings: Int,
    val sectors: Int,
    val startOffsets: List<Int>,
    val rotations: List<Int>,
    val targets: List<Int>,
    val lockedCount: Int,
    val par: Int,
    val decoySectors: List<Int>
) {
    fun offsetAt(ring: Int): Int = (startOffsets[ring] + rotations[ring]) % sectors

    fun turnsAt(ring: Int): Int = startOffsets[ring] + rotations[ring]

    fun statusAt(ring: Int): RingStatus = when {
        ring < lockedCount -> RingStatus.LOCKED
        ring == lockedCount -> RingStatus.LIVE
        else -> RingStatus.DORMANT
    }

    val solved: Boolean get() = lockedCount >= rings
}
