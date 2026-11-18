package com.Kohquqe3.Vurnaf.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary

@Composable
fun MarblePlate(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    fill: Color = SurfaceCard.copy(alpha = 0.95f),
    borderColor: Color = TextPrimary.copy(alpha = 0.12f),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 10.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        shape = shape,
        color = fill,
        border = BorderStroke(borderWidth, borderColor),
        modifier = modifier.shadow(elevation = elevation, shape = shape, clip = false)
    ) {
        Box(content = content)
    }
}
