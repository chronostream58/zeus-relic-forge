package com.Kohquqe3.Vurnaf.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.data.sample.SampleData
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.model.RoundOutcome
import com.Kohquqe3.Vurnaf.domain.model.ToolId
import com.Kohquqe3.Vurnaf.domain.repository.CatalogRepository
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository
import com.Kohquqe3.Vurnaf.domain.usecase.ApplyHintUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.AwardRewardsUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.GenerateBoardUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.RotateRingUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface GameEvent {
    data class Finished(val outcome: RoundOutcome) : GameEvent
}

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository,
    private val generateBoard: GenerateBoardUseCase,
    private val rotateRing: RotateRingUseCase,
    private val applyHint: ApplyHintUseCase,
    private val awardRewards: AwardRewardsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var progress: PlayerProgress = PlayerProgress.NEW_SAVE
    private var lastRotationLocked = false
    private var fragmentsEarned = 0
    private var startedLevel: LevelId? = null
    private var busyJob: Job? = null
    private var messageJob: Job? = null
    private var effectJob: Job? = null
    private var closingJob: Job? = null

    fun start(levelId: LevelId, challenge: Boolean) {
        if (startedLevel == levelId && _state.value.ready && !_state.value.finished) return
        startedLevel = levelId
        viewModelScope.launch {
            progress = progressRepository.load()
            buildRound(levelId, challenge)
        }
    }

    fun restart() {
        val current = _state.value
        if (!current.ready) return
        cancelPending()
        viewModelScope.launch {
            progress = progressRepository.load()
            buildRound(current.levelId, current.challenge)
        }
    }

    private fun buildRound(levelId: LevelId, challenge: Boolean) {
        val spec = catalogRepository.level(levelId)
        val board = generateBoard(spec)
        lastRotationLocked = false
        fragmentsEarned = 0
        _state.value = GameUiState(
            ready = true,
            levelId = levelId,
            chapterTitleRes = SampleData.chapterTitles[(levelId.chapter - 1).coerceIn(0, SampleData.chapterTitles.lastIndex)],
            board = board,
            charge = spec.charge + progress.startingChargeBonus,
            maxCharge = spec.charge + progress.startingChargeBonus,
            moves = 0,
            par = board.par,
            crystals = progress.crystals,
            challenge = challenge,
            gateNodes = spec.gateNodes,
            previewLevel = progress.toolLevel(ToolId.RESONANCE_CALIPER),
            messageRes = R.string.game_control_hint
        )
    }

    fun onRingTap(ring: Int) {
        val current = _state.value
        val board = current.board
        if (!current.ready || current.finished || current.paused || board == null) return
        if (current.busyRing == ring) return

        val result = rotateRing(board, ring)
        if (result.anchored) {
            lastRotationLocked = false
            val charge = (current.charge - result.chargeCost).coerceAtLeast(0)
            _state.update {
                it.copy(charge = charge, backlash = true, flashRing = ring, messageRes = R.string.game_anchored, messageArgs = emptyList())
            }
            scheduleEffectClear()
            scheduleMessageClear()
            if (charge <= 0) collapse()
            return
        }

        var charge = current.charge - result.chargeCost
        var resonances = current.resonances
        var messageRes = if (result.lockedNow) R.string.game_locked_ring else R.string.game_control_hint
        var messageArgs = emptyList<Int>()

        if (result.lockedNow && lastRotationLocked) {
            resonances += 1
            charge += GameConfig.RESONANCE_CHARGE
            fragmentsEarned += GameConfig.RESONANCE_FRAGMENTS
            messageRes = R.string.game_resonance
            messageArgs = listOf(resonances, GameConfig.RESONANCE_CHARGE, GameConfig.RESONANCE_FRAGMENTS)
        }
        lastRotationLocked = result.lockedNow

        val solved = result.board.solved
        if (solved) {
            messageRes = R.string.game_armed
            messageArgs = emptyList()
        }

        _state.update {
            it.copy(
                board = result.board,
                charge = charge.coerceAtLeast(0),
                moves = it.moves + 1,
                resonances = resonances,
                busyRing = ring,
                flashRing = if (result.lockedNow) ring else -1,
                messageRes = messageRes,
                messageArgs = messageArgs
            )
        }
        scheduleBusyClear()
        scheduleEffectClear()
        if (!solved) scheduleMessageClear()
        if (!solved && charge <= 0) collapse()
    }

    fun onSurge() {
        val current = _state.value
        val board = current.board
        if (!current.ready || current.finished || current.paused || board == null) return

        if (board.solved) {
            _state.update { it.copy(winFlare = true, finished = true) }
            closingJob?.cancel()
            closingJob = viewModelScope.launch {
                delay(GameConfig.WIN_FLARE_MS)
                finishRound(won = true)
            }
            return
        }

        lastRotationLocked = false
        val charge = (current.charge - GameConfig.SURGE_BACKLASH_COST).coerceAtLeast(0)
        _state.update {
            it.copy(
                charge = charge,
                shake = true,
                backlash = true,
                messageRes = R.string.game_unstable,
                messageArgs = emptyList()
            )
        }
        scheduleEffectClear()
        scheduleMessageClear()
        if (charge <= 0) collapse()
    }

    fun onHint() {
        val current = _state.value
        val board = current.board
        if (!current.ready || current.finished || current.paused || board == null) return

        if (current.crystals <= 0) {
            _state.update { it.copy(messageRes = R.string.game_no_crystals, messageArgs = emptyList()) }
            scheduleMessageClear()
            return
        }

        val snapped = applyHint(board)
        lastRotationLocked = false
        progress = progress.copy(crystals = current.crystals - 1)
        progressRepository.save(progress)
        val solved = snapped.solved
        _state.update {
            it.copy(
                board = snapped,
                crystals = it.crystals - 1,
                flashRing = snapped.lockedCount - 1,
                messageRes = if (solved) R.string.game_armed else R.string.game_locked_ring,
                messageArgs = emptyList()
            )
        }
        scheduleEffectClear()
        if (!solved) scheduleMessageClear()
    }

    fun onPauseRequested() {
        if (!_state.value.ready || _state.value.finished) return
        _state.update { it.copy(paused = true) }
    }

    fun onResume() {
        _state.update { it.copy(paused = false) }
    }

    fun pause() {
        cancelPending()
    }

    private fun collapse() {
        if (_state.value.finished) return
        _state.update { it.copy(finished = true) }
        closingJob?.cancel()
        closingJob = viewModelScope.launch {
            delay(GameConfig.COLLAPSE_MS)
            finishRound(won = false)
        }
    }

    private fun finishRound(won: Boolean) {
        val current = _state.value
        val withinPar = current.challenge && current.moves <= current.par
        val fragments = if (won) {
            GameConfig.GATE_FRAGMENTS + fragmentsEarned + if (withinPar) GameConfig.CHALLENGE_FRAGMENTS else 0
        } else {
            fragmentsEarned
        }
        val outcome = RoundOutcome(
            levelId = current.levelId,
            won = won,
            moves = current.moves,
            par = current.par,
            fragments = fragments,
            resonances = current.resonances,
            chargeRefunded = current.resonances * GameConfig.RESONANCE_CHARGE,
            challenge = current.challenge,
            sigilEarned = won && withinPar,
            nextLevel = if (won) current.levelId.next() else null
        )
        progress = awardRewards(progress, outcome)
        progressRepository.save(progress)
        startedLevel = null
        viewModelScope.launch { _events.send(GameEvent.Finished(outcome)) }
    }

    private fun scheduleBusyClear() {
        busyJob?.cancel()
        busyJob = viewModelScope.launch {
            delay(GameConfig.ROTATION_ANIM_MS.toLong())
            _state.update { it.copy(busyRing = -1) }
        }
    }

    private fun scheduleEffectClear() {
        effectJob?.cancel()
        effectJob = viewModelScope.launch {
            delay(GameConfig.BACKLASH_MS)
            _state.update { it.copy(backlash = false, shake = false, flashRing = -1) }
        }
    }

    private fun scheduleMessageClear() {
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            delay(GameConfig.MESSAGE_MS)
            _state.update { it.copy(messageRes = R.string.game_control_hint, messageArgs = emptyList()) }
        }
    }

    private fun cancelPending() {
        busyJob?.cancel()
        messageJob?.cancel()
        effectJob?.cancel()
        closingJob?.cancel()
    }

    override fun onCleared() {
        cancelPending()
        super.onCleared()
    }
}
