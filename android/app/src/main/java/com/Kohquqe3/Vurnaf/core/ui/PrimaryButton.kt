package com.Kohquqe3.Vurnaf.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.Bronze
import com.Kohquqe3.Vurnaf.core.theme.GoldHighlight
import com.Kohquqe3.Vurnaf.core.theme.OnGold
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    armed: Boolean = true,
    glow: Float = 0f,
    leadingIcon: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium)
    )
    val shape = RoundedCornerShape(16.dp)
    val fill = if (armed) {
        Brush.horizontalGradient(listOf(AccentGold, GoldHighlight))
    } else {
        Brush.horizontalGradient(listOf(SurfaceRaised, SurfaceRaised))
    }
    val labelColor = if (armed) OnGold else TextSecondary

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (armed) 10.dp else 2.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        modifier = modifier
            .height(60.dp)
            .scale(scale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = fill, shape = shape)
                .border(
                    width = if (armed) 0.dp else 1.dp,
                    color = if (armed) Color.Transparent else Bronze.copy(alpha = 0.45f + glow * 0.4f),
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (leadingIcon != null) {
                    Icon(
                        painter = painterResource(leadingIcon),
                        contentDescription = null,
                        tint = labelColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = labelColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
