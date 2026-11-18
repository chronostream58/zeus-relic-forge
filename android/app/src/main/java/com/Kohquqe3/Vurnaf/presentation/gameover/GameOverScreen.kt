package com.Kohquqe3.Vurnaf.presentation.gameover

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.di.AppViewModelFactory
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.core.theme.BacklashRed
import com.Kohquqe3.Vurnaf.core.theme.Patina
import com.Kohquqe3.Vurnaf.core.theme.StormBlue
import com.Kohquqe3.Vurnaf.core.theme.SurfaceWarm
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.PrimaryButton
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground
import com.Kohquqe3.Vurnaf.core.ui.SecondaryButton
import com.Kohquqe3.Vurnaf.core.ui.StatCardData
import com.Kohquqe3.Vurnaf.core.ui.StatRow
import com.Kohquqe3.Vurnaf.domain.model.RoundOutcome

@Composable
fun GameOverScreen(
    outcome: RoundOutcome,
    revision: Int,
    onPlayAgain: () -> Unit,
    onNextGate: () -> Unit,
    onWorkshop: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(revision) {
        gameOverViewModel.refresh()
    }

    GameOverContent(
        outcome = outcome,
        state = state,
        onPlayAgain = onPlayAgain,
        onNextGate = onNextGate,
        onWorkshop = onWorkshop,
        onMenu = onMenu,
        modifier = modifier
    )
}

@Composable
private fun GameOverContent(
    outcome: RoundOutcome,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onNextGate: () -> Unit,
    onWorkshop: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val won = outcome.won
    val overlay = if (won) {
        Brush.verticalGradient(
            listOf(SurfaceWarm.copy(alpha = 0.9f), BackgroundBase.copy(alpha = 0.95f))
        )
    } else {
        Brush.verticalGradient(
            listOf(BackgroundBase.copy(alpha = 0.95f), BackgroundBase.copy(alpha = 0.97f))
        )
    }

    var entered by remember { mutableStateOf(false) }
    val flare by animateFloatAsState(
        targetValue = if (entered) 1f else 1.15f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
    )
    LaunchedEffect(outcome) {
        entered = true
    }

    val cards = buildList {
        add(
            StatCardData(
                label = stringResource(R.string.stat_moves),
                value = outcome.moves.toString(),
                accent = TextPrimary
            )
        )
        add(
            StatCardData(
                label = stringResource(R.string.stat_par),
                value = outcome.par.toString(),
                accent = AccentGold
            )
        )
        if (outcome.fragments > 0) {
            add(
                StatCardData(
                    label = stringResource(R.string.stat_fragments),
                    value = stringResource(R.string.value_gain, outcome.fragments),
                    accent = StormBlue,
                    icon = painterResource(R.drawable.sprite_2),
                    iconDescription = stringResource(R.string.cd_fragment)
                )
            )
        }
        if (outcome.sigilEarned) {
            add(
                StatCardData(
                    label = stringResource(R.string.result_sigil),
                    value = stringResource(R.string.result_sigil_value),
                    accent = AccentGold,
                    highlighted = true
                )
            )
        }
    }

    ScreenBackground(overlay = overlay, modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(if (won) R.string.result_win_title else R.string.result_loss_title),
                style = MaterialTheme.typography.displaySmall,
                color = if (won) AccentGold else BacklashRed,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(if (won) R.string.result_win_subtitle else R.string.result_loss_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    (if (won) AccentGold else BacklashRed).copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                if (won) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_gate),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(170.dp)
                            .scale(flare)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_storm_core),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(140.dp)
                            .alpha(0.55f)
                    )
                }
            }

            StatRow(cards = cards)

            if (outcome.resonances > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceWarm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(
                            R.string.result_banner,
                            outcome.resonances,
                            outcome.chargeRefunded,
                            outcome.resonances
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = Patina,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (won && outcome.nextLevel != null) {
                PrimaryButton(
                    text = stringResource(R.string.cta_next_gate),
                    onClick = onNextGate,
                    leadingIcon = R.drawable.ic_play,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.cta_forge_again),
                        onClick = onPlayAgain,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = stringResource(R.string.cta_menu),
                        onClick = onMenu,
                        leadingPainter = painterResource(R.drawable.icon_home),
                        leadingDescription = stringResource(R.string.cd_home),
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                PrimaryButton(
                    text = stringResource(R.string.cta_forge_again),
                    onClick = onPlayAgain,
                    leadingIcon = R.drawable.ic_play,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.cta_workshop),
                        onClick = onWorkshop,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = stringResource(R.string.cta_menu),
                        onClick = onMenu,
                        leadingPainter = painterResource(R.drawable.icon_home),
                        leadingDescription = stringResource(R.string.cd_home),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.gates_progress, state.gatesPowered, state.totalGates),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
