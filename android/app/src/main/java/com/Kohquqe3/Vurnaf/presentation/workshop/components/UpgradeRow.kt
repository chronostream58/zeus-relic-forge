package com.Kohquqe3.Vurnaf.presentation.workshop.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.Bronze
import com.Kohquqe3.Vurnaf.core.theme.Patina
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.SurfaceWarm
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.PrimaryButton
import com.Kohquqe3.Vurnaf.presentation.workshop.ToolRowUi

@Composable
fun UpgradeRow(
    tool: ToolRowUi,
    onForge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unaffordableLabel = stringResource(R.string.state_unaffordable)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, TextPrimary.copy(alpha = 0.1f)),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 112.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(if (tool.mastered) AccentGold else Bronze)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(tool.nameRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(tool.effectRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(tool.maxLevel) { pip ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    color = if (pip < tool.level) AccentGold else SurfaceWarm,
                                    shape = CircleShape
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    if (!tool.mastered) {
                        Text(
                            text = stringResource(R.string.workshop_cost, tool.cost),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (tool.affordable) AccentGold else TextSecondary
                        )
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(128.dp)
                    .padding(horizontal = 10.dp, vertical = 14.dp)
            ) {
                if (tool.mastered) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceWarm,
                        border = BorderStroke(1.dp, Patina.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = stringResource(R.string.workshop_mastered),
                            style = MaterialTheme.typography.labelSmall,
                            color = Patina,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp)
                        )
                    }
                } else {
                    PrimaryButton(
                        text = stringResource(R.string.cta_forge),
                        onClick = onForge,
                        armed = tool.affordable,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                if (!tool.affordable) stateDescription = unaffordableLabel
                            }
                    )
                }
            }
        }
    }
}
