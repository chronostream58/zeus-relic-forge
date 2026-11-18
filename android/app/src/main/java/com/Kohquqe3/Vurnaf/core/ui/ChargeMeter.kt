package com.Kohquqe3.Vurnaf.core.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.BacklashRed
import com.Kohquqe3.Vurnaf.core.theme.StormBlue
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary

@Composable
fun ChargeMeter(
    current: Int,
    maximum: Int,
    readout: String,
    modifier: Modifier = Modifier
) {
    val ratio = if (maximum <= 0) 0f else (current.toFloat() / maximum.toFloat()).coerceIn(0f, 1f)
    val animatedRatio by animateFloatAsState(
        targetValue = ratio,
        animationSpec = tween(durationMillis = 300)
    )
    val critical = current <= 2
    val transition = rememberInfiniteTransition()
    val pulse by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse
        )
    )
    val fill = if (critical) {
        Brush.horizontalGradient(listOf(BacklashRed, BacklashRed))
    } else {
        Brush.horizontalGradient(listOf(StormBlue, AccentGold))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .background(SurfaceRaised, RoundedCornerShape(7.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedRatio.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .alpha(if (critical) pulse else 1f)
                    .background(fill, RoundedCornerShape(7.dp))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = readout,
            style = MaterialTheme.typography.titleMedium,
            color = if (critical) BacklashRed else TextPrimary,
            maxLines = 1,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
        )
    }
}
