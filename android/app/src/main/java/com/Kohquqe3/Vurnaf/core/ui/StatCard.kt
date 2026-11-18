package com.Kohquqe3.Vurnaf.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary

data class StatCardData(
    val label: String,
    val value: String,
    val accent: Color,
    val icon: Painter? = null,
    val iconDescription: String? = null,
    val highlighted: Boolean = false
)

@Composable
fun StatCard(
    data: StatCardData,
    modifier: Modifier = Modifier
) {
    if (data.value.isBlank()) return
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard.copy(alpha = if (data.highlighted) 0.96f else 0.88f),
        border = BorderStroke(1.dp, data.accent.copy(alpha = if (data.highlighted) 0.75f else 0.33f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (data.icon != null) {
                    Image(
                        painter = data.icon,
                        contentDescription = data.iconDescription,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                } else {
                    Spacer(
                        modifier = Modifier
                            .size(8.dp)
                            .background(data.accent, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = data.value,
                    style = MaterialTheme.typography.titleMedium,
                    color = data.accent,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StatRow(
    cards: List<StatCardData>,
    modifier: Modifier = Modifier
) {
    val visible = cards.filter { it.value.isNotBlank() }
    if (visible.size < 2) return
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        visible.forEach { card ->
            StatCard(
                data = card,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
