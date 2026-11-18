package com.Kohquqe3.Vurnaf.domain.usecase

import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.domain.model.BoardState
import com.Kohquqe3.Vurnaf.domain.model.LevelSpec
import kotlin.random.Random

class GenerateBoardUseCase {

    operator fun invoke(spec: LevelSpec): BoardState {
        val seed = (GameConfig.SEED_PRIME_A * spec.id.chapter) xor (GameConfig.SEED_PRIME_B * spec.id.index)
        val random = Random(seed)
        var fallback: BoardState? = null
        repeat(GameConfig.GENERATION_ATTEMPTS) {
            val targets = List(spec.rings) { random.nextInt(spec.sectors) }
            val steps = List(spec.rings) { 1 + random.nextInt(spec.sectors - 1) }
            val candidate = build(spec, targets, steps, random)
            if (candidate.par in spec.parMin..spec.parMax) return candidate
            if (fallback == null) fallback = candidate
        }
        return fallback ?: build(spec, List(spec.rings) { 0 }, evenSteps(spec), random)
    }

    private fun evenSteps(spec: LevelSpec): List<Int> {
        val average = (spec.parMin + spec.parMax) / (2 * spec.rings)
        val step = average.coerceIn(1, spec.sectors - 1)
        return List(spec.rings) { step }
    }

    private fun build(spec: LevelSpec, targets: List<Int>, steps: List<Int>, random: Random): BoardState {
        val starts = List(spec.rings) { ring ->
            ((targets[ring] - steps[ring]) % spec.sectors + spec.sectors) % spec.sectors
        }
        val decoys = if (spec.decoys) {
            List(spec.rings) { random.nextInt(spec.sectors) }
        } else {
            List(spec.rings) { -1 }
        }
        return BoardState(
            rings = spec.rings,
            sectors = spec.sectors,
            startOffsets = starts,
            rotations = List(spec.rings) { 0 },
            targets = targets,
            lockedCount = 0,
            par = steps.sum(),
            decoySectors = decoys
        )
    }
}
