package com.Kohquqe3.Vurnaf.presentation.levels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.core.di.AppViewModelFactory
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground
import com.Kohquqe3.Vurnaf.core.ui.SectionHeader
import com.Kohquqe3.Vurnaf.domain.model.LevelId
import com.Kohquqe3.Vurnaf.presentation.levels.components.LevelTile

@Composable
fun LevelSelectScreen(
    revision: Int,
    onStartLevel: (LevelId, Boolean) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    levelSelectViewModel: LevelSelectViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by levelSelectViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(revision) {
        levelSelectViewModel.refresh()
    }

    LevelSelectContent(
        state = state,
        onStartLevel = { id -> onStartLevel(id, levelSelectViewModel.isChallengeArmed(id)) },
        onSealedTap = levelSelectViewModel::onSealedTap,
        onToggleChallenge = levelSelectViewModel::toggleChallenge,
        onMenu = onMenu,
        modifier = modifier
    )
}

@Composable
private fun LevelSelectContent(
    state: LevelSelectUiState,
    onStartLevel: (LevelId) -> Unit,
    onSealedTap: (LevelId) -> Unit,
    onToggleChallenge: (LevelId) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overlay = Brush.verticalGradient(
        listOf(BackgroundBase.copy(alpha = 0.92f), SurfaceCard.copy(alpha = 0.95f))
    )

    ScreenBackground(overlay = overlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 12.dp)
            ) {
                IconButton(
                    onClick = onMenu,
                    modifier = Modifier.size(48.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = stringResource(R.string.cd_home),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.levels_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                state.chapters.forEach { chapter ->
                    item(key = "chapter-" + chapter.chapter) {
                        SectionHeader(
                            title = stringResource(chapter.titleRes),
                            trailing = stringResource(
                                R.string.chapter_sigils,
                                chapter.sigils,
                                GameConfig.LEVELS_PER_CHAPTER
                            )
                        )
                    }
                    items(
                        count = chapter.levels.size,
                        key = { index -> "level-" + chapter.levels[index].id.key }
                    ) { index ->
                        val tile = chapter.levels[index]
                        LevelTile(
                            tile = tile,
                            sealedMessageVisible = state.messageLevel == tile.id,
                            onStart = { onStartLevel(tile.id) },
                            onSealedTap = { onSealedTap(tile.id) },
                            onToggleChallenge = { onToggleChallenge(tile.id) }
                        )
                    }
                }
            }
        }
    }
}
