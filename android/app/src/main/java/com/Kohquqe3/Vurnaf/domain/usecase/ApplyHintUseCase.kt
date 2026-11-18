package com.Kohquqe3.Vurnaf.domain.usecase

import com.Kohquqe3.Vurnaf.domain.model.BoardState

class ApplyHintUseCase {

    operator fun invoke(board: BoardState): BoardState {
        val ring = board.lockedCount
        if (ring >= board.rings) return board
        val needed = ((board.targets[ring] - board.offsetAt(ring)) % board.sectors + board.sectors) % board.sectors
        val rotations = board.rotations.toMutableList()
        rotations[ring] = rotations[ring] + needed
        val snapped = board.copy(rotations = rotations)
        var locked = ring
        while (locked < snapped.rings && snapped.offsetAt(locked) == snapped.targets[locked]) {
            locked++
        }
        return snapped.copy(lockedCount = locked)
    }
}
