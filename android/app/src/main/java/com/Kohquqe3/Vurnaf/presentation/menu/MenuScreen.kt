package com.Kohquqe3.Vurnaf.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Kohquqe3.Vurnaf.core.theme.StormBlue
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextMuted
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.PrimaryButton
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground
import com.Kohquqe3.Vurnaf.core.ui.SecondaryButton
import com.Kohquqe3.Vurnaf.core.ui.StatCardData
import com.Kohquqe3.Vurnaf.core.ui.StatRow
import com.Kohquqe3.Vurnaf.domain.model.LevelId

@Composable
fun MenuScreen(
    revision: Int,
    onPlay: (LevelId, Boolean) -> Unit,
    onLevels: () -> Unit,
    onWorkshop: () -> Unit,
    onLore: () -> Unit,
    modifier: Modifier = Modifier,
    menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(revision) {
        menuViewModel.refresh()
    }

    MenuContent(
        state = state,
        onPlay = { onPlay(state.nextLevel, state.challengeArmed) },
        onLevels = onLevels,
        onWorkshop = onWorkshop,
        onLore = onLore,
        modifier = modifier
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onWorkshop: () -> Unit,
    onLore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overlay = Brush.verticalGradient(
        listOf(
            BackgroundBase.copy(alpha = 0.2f),
            BackgroundBase.copy(alpha = 0.6f),
            BackgroundBase.copy(alpha = 0.96f)
        )
    )
    val transition = rememberInfiniteTransition()
    val archBreath by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(tween(4200), repeatMode = RepeatMode.Reverse)
    )

    val cards = listOf(
        StatCardData(
            label = stringResource(R.string.stat_gates),
            value = if (state.gatesPowered > 0) {
                stringResource(R.string.value_fraction, state.gatesPowered, state.totalGates)
            } else {
                ""
            },
            accent = AccentGold
        ),
        StatCardData(
            label = stringResource(R.string.stat_fragments),
            value = if (state.fragments > 0) state.fragments.toString() else "",
            accent = StormBlue,
            icon = painterResource(R.drawable.sprite_2),
            iconDescription = stringResource(R.string.cd_fragment)
        ),
        StatCardData(
            label = stringResource(R.string.stat_crystals),
            value = if (state.crystals > 0) state.crystals.toString() else "",
            accent = TextPrimary,
            icon = painterResource(R.drawable.sprite_3),
            iconDescription = stringResource(R.string.cd_crystal)
        )
    )
    val visibleCards = cards.count { it.value.isNotBlank() }

    ScreenBackground(overlay = overlay, modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(AccentGold.copy(alpha = 0.22f), Color.Transparent)
                            )
                        )
                )
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_gate),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)
                        .scale(archBreath)
                )
            }

            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = SurfaceCard.copy(alpha = 0.96f),
                border = BorderStroke(1.dp, TextPrimary.copy(alpha = 0.12f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .safeDrawingPadding()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(3.dp)
                            .background(AccentGold, RoundedCornerShape(2.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(148.dp)
                            .height(64.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.brand_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (visibleCards >= 2) {
                        StatRow(cards = cards)
                    } else {
                        Text(
                            text = stringResource(R.string.menu_empty),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.menu_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PrimaryButton(
                        text = stringResource(R.string.cta_play),
                        onClick = onPlay,
                        leadingIcon = R.drawable.ic_play,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.cta_levels),
                            onClick = onLevels,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.cta_workshop),
                            onClick = onWorkshop,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.cta_lore),
                            onClick = onLore,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.menu_disclaimer),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
