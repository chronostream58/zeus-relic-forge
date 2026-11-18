package com.Kohquqe3.Vurnaf.presentation.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.data.sample.SampleData
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.repository.CatalogRepository
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelSelectViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelSelectUiState())
    val state: StateFlow<LevelSelectUiState> = _state.asStateFlow()

    private var progress: PlayerProgress = PlayerProgress.NEW_SAVE

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            progress = progressRepository.load()
            _state.update { it.copy(loaded = true, chapters = buildChapters(), messageLevel = null) }
        }
    }

    fun toggleChallenge(id: LevelId) {
        val armed = progress.challengeArmed
        val updated = if (armed.contains(id.key)) armed - id.key else armed + id.key
        progress = progress.copy(challengeArmed = updated)
        progressRepository.save(progress)
        _state.update { it.copy(chapters = buildChapters()) }
    }

    fun onSealedTap(id: LevelId) {
        _state.update { it.copy(messageLevel = id) }
    }

    fun isChallengeArmed(id: LevelId): Boolean = progress.isChallengeArmed(id)

    private fun buildChapters(): List<ChapterUi> {
        val specs = catalogRepository.levels()
        return (1..GameConfig.CHAPTERS).map { chapter ->
            val tiles = specs.filter { it.id.chapter == chapter }.map { spec ->
                LevelTileUi(
                    id = spec.id,
                    rings = spec.rings,
                    sectors = spec.sectors,
                    charge = spec.charge,
                    unlocked = progress.isUnlocked(spec.id),
                    powered = progress.isPowered(spec.id),
                    bestMoves = progress.bestMoves[spec.id.key] ?: 0,
                    sigil = progress.hasSigil(spec.id),
                    challengeArmed = progress.isChallengeArmed(spec.id),
                    unlockAfter = spec.id.previous()
                )
            }
            ChapterUi(
                chapter = chapter,
                titleRes = SampleData.chapterTitles[chapter - 1],
                sigils = progress.sigilsInChapter(chapter),
                levels = tiles
            )
        }
    }
}
