package com.Kohquqe3.Vurnaf.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.di.AppViewModelFactory
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.core.theme.BacklashRed
import com.Kohquqe3.Vurnaf.core.theme.Bronze
import com.Kohquqe3.Vurnaf.core.theme.StormBlue
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.SurfaceWarm
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.ChargeMeter
import com.Kohquqe3.Vurnaf.core.ui.PrimaryButton
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground
import com.Kohquqe3.Vurnaf.core.ui.SecondaryButton
import com.Kohquqe3.Vurnaf.core.ui.StatCardData
import com.Kohquqe3.Vurnaf.core.ui.StatRow
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.domain.model.RoundOutcome
import com.Kohquqe3.Vurnaf.presentation.dialog.PauseDialog
import com.Kohquqe3.Vurnaf.presentation.game.components.RingBoard

@Composable
fun GameScreen(
    levelId: LevelId,
    challenge: Boolean,
    attempt: Int,
    onFinished: (RoundOutcome) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(levelId, challenge, attempt) {
        gameViewModel.start(levelId, challenge)
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.outcome)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    BackHandler(enabled = true) {
        if (state.paused) gameViewModel.onResume() else gameViewModel.onPauseRequested()
    }

    GameContent(
        state = state,
        onRingTap = gameViewModel::onRingTap,
        onSurge = gameViewModel::onSurge,
        onHint = gameViewModel::onHint,
        onPause = gameViewModel::onPauseRequested,
        modifier = modifier
    )

    if (state.paused) {
        PauseDialog(
            onResume = gameViewModel::onResume,
            onRestart = {
                gameViewModel.onResume()
                gameViewModel.restart()
            },
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onRingTap: (Int) -> Unit,
    onSurge: () -> Unit,
    onHint: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overlay = Brush.verticalGradient(
        listOf(
            BackgroundBase.copy(alpha = 0.9f),
            SurfaceRaised.copy(alpha = 0.95f),
            SurfaceWarm.copy(alpha = 0.95f)
        )
    )
    val board = state.board
    val armedLabel = stringResource(R.string.state_armed)
    val unstableLabel = stringResource(R.string.state_unstable)

    ScreenBackground(overlay = overlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Surface(
                color = BackgroundBase.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    IconButton(
                        onClick = onPause,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_pause),
                            contentDescription = stringResource(R.string.cd_pause),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (state.chapterTitleRes != 0) {
                                stringResource(
                                    R.string.gate_title,
                                    stringResource(state.chapterTitleRes),
                                    state.levelId.chapter,
                                    state.levelId.index
                                )
                            } else {
                                stringResource(R.string.game_preparing)
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        if (state.challenge) {
                            Text(
                                text = stringResource(R.string.game_challenge_route, state.par),
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentGold,
                                maxLines = 1
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceRaised.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, StormBlue.copy(alpha = 0.35f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.sprite_3),
                                contentDescription = stringResource(R.string.cd_crystal),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = state.crystals.toString(),
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (board == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = AccentGold)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = stringResource(R.string.game_preparing),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                } else {
                    RingBoard(
                        rings = board.rings,
                        sectors = board.sectors,
                        turns = List(board.rings) { board.turnsAt(it) },
                        lockedCount = board.lockedCount,
                        decoySectors = board.decoySectors,
                        gateNodes = state.gateNodes,
                        previewLevel = state.previewLevel,
                        flashRing = state.flashRing,
                        backlash = state.backlash,
                        shake = state.shake,
                        solved = board.solved,
                        anchoredLabel = stringResource(
                            if (board.solved) R.string.state_armed else R.string.state_unstable
                        ),
                        onRingTap = onRingTap,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Text(
                text = messageText(state),
                style = MaterialTheme.typography.labelSmall,
                color = if (state.backlash) BacklashRed else TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )

            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = SurfaceCard.copy(alpha = 0.97f),
                border = BorderStroke(1.dp, Bronze.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    ChargeMeter(
                        current = state.charge,
                        maximum = state.maxCharge,
                        readout = stringResource(R.string.game_charge, state.charge, state.maxCharge)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StatRow(
                        cards = listOf(
                            StatCardData(
                                label = stringResource(R.string.stat_moves),
                                value = state.moves.toString(),
                                accent = TextPrimary
                            ),
                            StatCardData(
                                label = stringResource(R.string.stat_par),
                                value = if (state.challenge) state.par.toString() else "",
                                accent = AccentGold
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PrimaryButton(
                        text = stringResource(R.string.cta_surge),
                        onClick = onSurge,
                        armed = state.armed,
                        leadingIcon = R.drawable.ic_bolt,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                stateDescription = if (state.armed) armedLabel else unstableLabel
                            }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SecondaryButton(
                        text = stringResource(R.string.cta_hint),
                        onClick = onHint,
                        leadingPainter = painterResource(R.drawable.sprite_3),
                        leadingDescription = stringResource(R.string.cd_crystal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun messageText(state: GameUiState): String {
    val resource = if (state.messageRes != 0) state.messageRes else R.string.game_control_hint
    return when (state.messageArgs.size) {
        3 -> stringResource(
            resource,
            state.messageArgs[0],
            state.messageArgs[1],
            state.messageArgs[2]
        )
        1 -> stringResource(resource, state.messageArgs[0])
        else -> stringResource(resource)
    }
}
