package com.Kohquqe3.Vurnaf.presentation.lore

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.di.AppViewModelFactory
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground
import com.Kohquqe3.Vurnaf.presentation.lore.components.LoreEntryCard

@Composable
fun LoreScreen(
    revision: Int,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    loreViewModel: LoreViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by loreViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(revision) {
        loreViewModel.refresh()
    }

    LoreContent(
        state = state,
        onToggle = loreViewModel::toggleExpanded,
        onMenu = onMenu,
        modifier = modifier
    )
}

@Composable
private fun LoreContent(
    state: LoreUiState,
    onToggle: (Int) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overlay = Brush.verticalGradient(
        listOf(BackgroundBase.copy(alpha = 0.95f), SurfaceRaised.copy(alpha = 0.96f))
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.lore_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.lore_progress, state.revealedCount, state.total),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = state.entries.size,
                    key = { index -> "lore-" + state.entries[index].id }
                ) { index ->
                    val entry = state.entries[index]
                    LoreEntryCard(
                        entry = entry,
                        expanded = state.expandedId == entry.id,
                        onToggle = { onToggle(entry.id) }
                    )
                }
                if (state.loaded && state.entries.isEmpty()) {
                    item(key = "lore-empty") {
                        Text(
                            text = stringResource(R.string.menu_empty),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
