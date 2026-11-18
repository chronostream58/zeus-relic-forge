package com.Kohquqe3.Vurnaf.presentation.lore.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextLore
import com.Kohquqe3.Vurnaf.core.theme.TextMuted
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.domain.model.LoreMedallion
import com.Kohquqe3.Vurnaf.presentation.lore.LoreEntryUi

@Composable
fun LoreEntryCard(
    entry: LoreEntryUi,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sealedLabel = stringResource(R.string.state_locked)
    val armedLabel = stringResource(R.string.state_armed)
    val medallion = when (entry.medallion) {
        LoreMedallion.FRAGMENT -> R.drawable.sprite_2
        LoreMedallion.CRYSTAL -> R.drawable.sprite_3
        LoreMedallion.GATE -> R.drawable.sprite_4
    }
    val medallionDescription = when (entry.medallion) {
        LoreMedallion.FRAGMENT -> R.string.cd_fragment
        LoreMedallion.CRYSTAL -> R.string.cd_crystal
        LoreMedallion.GATE -> R.string.cd_gate
    }

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard.copy(alpha = 0.92f),
        border = BorderStroke(1.dp, if (entry.revealed) AccentGold.copy(alpha = 0.3f) else TextPrimary.copy(alpha = 0.08f)),
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (entry.revealed) 1f else 0.5f)
            .animateContentSize(animationSpec = tween(durationMillis = 240))
            .semantics { stateDescription = if (entry.revealed) armedLabel else sealedLabel }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 76.dp)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(medallion),
                contentDescription = stringResource(medallionDescription),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.Top)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(entry.titleRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (entry.revealed) TextPrimary else TextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (entry.revealed) {
                    Text(
                        text = stringResource(entry.bodyRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextLore,
                        maxLines = if (expanded) 12 else 2
                    )
                } else {
                    Text(
                        text = if (entry.bonus) {
                            stringResource(R.string.lore_bonus_sealed)
                        } else {
                            stringResource(R.string.lore_sealed, entry.gate.chapter, entry.gate.index)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
