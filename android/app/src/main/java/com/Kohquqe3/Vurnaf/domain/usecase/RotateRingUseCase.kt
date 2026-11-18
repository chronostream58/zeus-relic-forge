package com.Kohquqe3.Vurnaf.domain.usecase

import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.model.BoardState
import com.Kohquqe3.Vurnaf.domain.model.RotationResult

class RotateRingUseCase {

    operator fun invoke(board: BoardState, ring: Int): RotationResult {
        if (ring < 0 || ring >= board.rings) {
            return RotationResult(board, anchored = false, lockedNow = false, chargeCost = 0)
        }
        if (ring < board.lockedCount) {
            return RotationResult(board, anchored = true, lockedNow = false, chargeCost = GameConfig.ANCHORED_COST)
        }
        val rotations = board.rotations.toMutableList()
        rotations[ring] = rotations[ring] + 1
        val turned = board.copy(rotations = rotations)
        var locked = board.lockedCount
        while (locked < turned.rings && turned.offsetAt(locked) == turned.targets[locked]) {
            locked++
        }
        return RotationResult(
            board = turned.copy(lockedCount = locked),
            anchored = false,
            lockedNow = locked > board.lockedCount,
            chargeCost = GameConfig.ROTATION_COST
        )
    }
}
