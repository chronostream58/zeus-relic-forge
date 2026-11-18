package com.Kohquqe3.Vurnaf.data.sample

import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.LevelSpec
import com.Kohquqe3.Vurnaf.domain.model.LoreEntry
import com.Kohquqe3.Vurnaf.domain.model.LoreMedallion
import com.Kohquqe3.Vurnaf.domain.model.ToolDefinition
import com.Kohquqe3.Vurnaf.domain.model.ToolId

object SampleData {

    val chapterTitles = listOf(R.string.chapter_one, R.string.chapter_two, R.string.chapter_three)

    private val antechamberCharges = listOf(12, 12, 11, 10, 10)
    private val conduitCharges = listOf(16, 15, 14, 13, 13)
    private val apexCharges = listOf(20, 19, 18, 17, 16)

    val levels: List<LevelSpec> = buildList {
        antechamberCharges.forEachIndexed { slot, charge ->
            add(
                LevelSpec(
                    id = LevelId(1, slot + 1),
                    rings = 3,
                    sectors = 6,
                    charge = charge,
                    parMin = 5,
                    parMax = 8,
                    gateNodes = 1,
                    decoys = false
                )
            )
        }
        conduitCharges.forEachIndexed { slot, charge ->
            add(
                LevelSpec(
                    id = LevelId(2, slot + 1),
                    rings = 4,
                    sectors = 6,
                    charge = charge,
                    parMin = 8,
                    parMax = 12,
                    gateNodes = 1,
                    decoys = true
                )
            )
        }
        apexCharges.forEachIndexed { slot, charge ->
            add(
                LevelSpec(
                    id = LevelId(3, slot + 1),
                    rings = 4,
                    sectors = 8,
                    charge = charge,
                    parMin = 12,
                    parMax = 16,
                    gateNodes = 2,
                    decoys = true
                )
            )
        }
    }

    val tools: List<ToolDefinition> = listOf(
        ToolDefinition(
            id = ToolId.ROTATION_DIAL,
            nameRes = R.string.tool_rotation_dial,
            effectRes = listOf(
                R.string.tool_rotation_dial_0,
                R.string.tool_rotation_dial_1,
                R.string.tool_rotation_dial_2,
                R.string.tool_rotation_dial_3
            )
        ),
        ToolDefinition(
            id = ToolId.RESONANCE_CALIPER,
            nameRes = R.string.tool_resonance_caliper,
            effectRes = listOf(
                R.string.tool_resonance_caliper_0,
                R.string.tool_resonance_caliper_1,
                R.string.tool_resonance_caliper_2,
                R.string.tool_resonance_caliper_3
            )
        ),
        ToolDefinition(
            id = ToolId.CRYSTAL_LATHE,
            nameRes = R.string.tool_crystal_lathe,
            effectRes = listOf(
                R.string.tool_crystal_lathe_0,
                R.string.tool_crystal_lathe_1,
                R.string.tool_crystal_lathe_2,
                R.string.tool_crystal_lathe_3
            )
        )
    )

    private val loreTitles = listOf(
        R.string.lore_title_1, R.string.lore_title_2, R.string.lore_title_3,
        R.string.lore_title_4, R.string.lore_title_5, R.string.lore_title_6,
        R.string.lore_title_7, R.string.lore_title_8, R.string.lore_title_9,
        R.string.lore_title_10, R.string.lore_title_11, R.string.lore_title_12,
        R.string.lore_title_13, R.string.lore_title_14, R.string.lore_title_15,
        R.string.lore_title_16, R.string.lore_title_17, R.string.lore_title_18
    )

    private val loreBodies = listOf(
        R.string.lore_body_1, R.string.lore_body_2, R.string.lore_body_3,
        R.string.lore_body_4, R.string.lore_body_5, R.string.lore_body_6,
        R.string.lore_body_7, R.string.lore_body_8, R.string.lore_body_9,
        R.string.lore_body_10, R.string.lore_body_11, R.string.lore_body_12,
        R.string.lore_body_13, R.string.lore_body_14, R.string.lore_body_15,
        R.string.lore_body_16, R.string.lore_body_17, R.string.lore_body_18
    )

    private val medallions = listOf(LoreMedallion.GATE, LoreMedallion.FRAGMENT, LoreMedallion.CRYSTAL)

    val lore: List<LoreEntry> = buildList {
        levels.forEachIndexed { slot, spec ->
            add(
                LoreEntry(
                    id = spec.id.ordinal,
                    titleRes = loreTitles[slot],
                    bodyRes = loreBodies[slot],
                    medallion = medallions[slot % medallions.size],
                    gate = spec.id,
                    bonus = false
                )
            )
        }
        for (chapter in 1..chapterTitles.size) {
            val slot = levels.size + chapter - 1
            add(
                LoreEntry(
                    id = levels.size + chapter,
                    titleRes = loreTitles[slot],
                    bodyRes = loreBodies[slot],
                    medallion = LoreMedallion.GATE,
                    gate = LevelId(chapter, 1),
                    bonus = true
                )
            )
        }
    }
}
