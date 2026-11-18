package com.Kohquqe3.Vurnaf.presentation.workshop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.SurfaceWarm
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.ConfirmDialog
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground
import com.Kohquqe3.Vurnaf.domain.model.ToolId
import com.Kohquqe3.Vurnaf.presentation.workshop.components.UpgradeRow

@Composable
fun WorkshopScreen(
    revision: Int,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    workshopViewModel: WorkshopViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by workshopViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(revision) {
        workshopViewModel.refresh()
    }

    WorkshopContent(
        state = state,
        onForge = workshopViewModel::onForgeRequested,
        onMenu = onMenu,
        modifier = modifier
    )

    val pending = state.pending
    if (pending != null) {
        val definition = state.tools.firstOrNull { it.id == pending }
        ConfirmDialog(
            title = stringResource(definition?.nameRes ?: R.string.workshop_title),
            body = stringResource(R.string.workshop_confirm_body, state.pendingCost),
            confirmText = stringResource(R.string.cta_forge),
            dismissText = stringResource(R.string.cta_cancel),
            onConfirm = workshopViewModel::onConfirm,
            onDismiss = workshopViewModel::onDismiss
        )
    }
}

@Composable
private fun WorkshopContent(
    state: WorkshopUiState,
    onForge: (ToolId) -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overlay = Brush.verticalGradient(
        listOf(SurfaceWarm.copy(alpha = 0.95f), BackgroundBase.copy(alpha = 0.96f))
    )

    ScreenBackground(overlay = overlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterEnd)
                        .alpha(0.18f)
                )
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
                        text = stringResource(R.string.workshop_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceRaised.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.35f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.sprite_2),
                                contentDescription = stringResource(R.string.cd_fragment),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = stringResource(R.string.workshop_balance, state.fragments),
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentGold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            if (state.messageRes != 0) {
                Text(
                    text = if (state.messageArg > 0) {
                        stringResource(state.messageRes, state.messageArg)
                    } else {
                        stringResource(state.messageRes)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 6.dp)
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = state.tools.size,
                    key = { index -> state.tools[index].id.name }
                ) { index ->
                    val tool = state.tools[index]
                    UpgradeRow(
                        tool = tool,
                        onForge = { onForge(tool.id) }
                    )
                }
                if (state.loaded && state.tools.isEmpty()) {
                    item(key = "workshop-empty") {
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
