package com.Kohquqe3.Vurnaf.presentation.levels.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.Bronze
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.TextMuted
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.presentation.levels.LevelTileUi

@Composable
fun LevelTile(
    tile: LevelTileUi,
    sealedMessageVisible: Boolean,
    onStart: () -> Unit,
    onSealedTap: () -> Unit,
    onToggleChallenge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val armedLabel = stringResource(R.string.state_armed)
    val disarmedLabel = stringResource(R.string.state_disarmed)
    val sealedLabel = stringResource(R.string.state_locked)

    Surface(
        onClick = { if (tile.unlocked) onStart() else onSealedTap() },
        shape = RoundedCornerShape(14.dp),
        color = SurfaceRaised.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, if (tile.sigil) AccentGold.copy(alpha = 0.6f) else TextPrimary.copy(alpha = 0.1f)),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 86.dp)
            .alpha(if (tile.unlocked) 1f else 0.45f)
            .semantics { stateDescription = if (tile.unlocked) armedLabel else sealedLabel }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.gate_label, tile.id.chapter, tile.id.index),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.level_descriptor, tile.rings, tile.sectors, tile.charge),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (tile.powered) {
                        stringResource(R.string.level_best, tile.bestMoves)
                    } else if (!tile.unlocked && tile.unlockAfter != null) {
                        stringResource(
                            R.string.level_sealed,
                            tile.unlockAfter.chapter,
                            tile.unlockAfter.index
                        )
                    } else {
                        stringResource(R.string.level_best_none)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (tile.powered) AccentGold else TextMuted
                )
                if (sealedMessageVisible && !tile.unlocked && tile.unlockAfter != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            R.string.level_sealed,
                            tile.unlockAfter.chapter,
                            tile.unlockAfter.index
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentGold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (tile.sigil) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = stringResource(R.string.cd_fragment),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                if (tile.unlocked) {
                    OutlinedButton(
                        onClick = onToggleChallenge,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (tile.challengeArmed) AccentGold else Bronze.copy(alpha = 0.45f)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .semantics {
                                stateDescription = if (tile.challengeArmed) armedLabel else disarmedLabel
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.cta_challenge),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (tile.challengeArmed) AccentGold else TextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
