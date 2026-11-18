package com.Kohquqe3.Vurnaf.presentation.game.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kohquqe3.Vurnaf.R
import com.Kohquqe3.Vurnaf.core.config.GameConfig
import com.Kohquqe3.Vurnaf.core.theme.AccentGold
import com.Kohquqe3.Vurnaf.core.theme.BacklashRed
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.core.theme.Bronze
import com.Kohquqe3.Vurnaf.core.theme.DormantStone
import com.Kohquqe3.Vurnaf.core.theme.GoldHighlight
import com.Kohquqe3.Vurnaf.core.theme.MarbleVeil
import com.Kohquqe3.Vurnaf.core.theme.StormBlue
import com.Kohquqe3.Vurnaf.core.theme.SurfaceCard
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun RingBoard(
    rings: Int,
    sectors: Int,
    turns: List<Int>,
    lockedCount: Int,
    decoySectors: List<Int>,
    gateNodes: Int,
    previewLevel: Int,
    flashRing: Int,
    backlash: Boolean,
    shake: Boolean,
    solved: Boolean,
    anchoredLabel: String,
    onRingTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val limit = if (maxWidth < maxHeight) maxWidth else maxHeight
        val boardSize = (limit - 32.dp).coerceIn(160.dp, 360.dp)
        val radius = boardSize / 2f
        val outerRadius = radius - 10.dp
        val coreRadius = radius * 0.21f
        val band = (outerRadius - coreRadius) / rings.toFloat()

        val sectorAngle = 360f / sectors
        val animatedAngles = List(GameConfig.MAX_RINGS) { index ->
            animateFloatAsState(
                targetValue = if (index < rings) turns.getOrElse(index) { 0 } * sectorAngle else 0f,
                animationSpec = tween(GameConfig.ROTATION_ANIM_MS, easing = FastOutSlowInEasing)
            )
        }
        val shakeOffset by animateFloatAsState(
            targetValue = if (shake) 8f else 0f,
            animationSpec = tween(130)
        )
        val transition = rememberInfiniteTransition()
        val corePulse by transition.animateFloat(
            initialValue = 1f,
            targetValue = if (solved) 1.14f else 1.06f,
            animationSpec = infiniteRepeatable(tween(if (solved) 1100 else 1600), repeatMode = RepeatMode.Reverse)
        )
        val gateGlow by transition.animateFloat(
            initialValue = 0.55f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1800), repeatMode = RepeatMode.Reverse)
        )

        Box(
            modifier = Modifier
                .size(boardSize)
                .offset(x = shakeOffset.dp)
                .semantics { stateDescription = anchoredLabel },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = SurfaceCard.copy(alpha = 0.94f),
                border = BorderStroke(3.dp, if (backlash) BacklashRed else Bronze),
                modifier = Modifier
                    .size(boardSize)
                    .shadow(elevation = 14.dp, shape = CircleShape, clip = false)
            ) {
                Box(modifier = Modifier.fillMaxSize())
            }

            Canvas(
                modifier = Modifier
                    .size(boardSize)
                    .pointerInput(rings, sectors, lockedCount, boardSize) {
                        detectTapGestures { position ->
                            val centre = Offset(size.width / 2f, size.height / 2f)
                            val distance = hypot(position.x - centre.x, position.y - centre.y)
                            val inner = coreRadius.toPx()
                            val outer = outerRadius.toPx()
                            if (distance in inner..outer) {
                                val ring = ((distance - inner) / band.toPx()).toInt().coerceIn(0, rings - 1)
                                onRingTap(ring)
                            }
                        }
                    }
            ) {
                val centre = Offset(size.width / 2f, size.height / 2f)
                val corePx = coreRadius.toPx()
                val bandPx = band.toPx()

                for (ring in 0 until rings) {
                    val innerPx = corePx + bandPx * ring
                    val outerPx = corePx + bandPx * (ring + 1)
                    val midPx = (innerPx + outerPx) / 2f
                    val locked = ring < lockedCount
                    val live = ring == lockedCount

                    drawCircle(
                        color = MarbleVeil.copy(alpha = if (locked) 0.12f else 0.07f),
                        radius = midPx,
                        center = centre,
                        style = Stroke(width = bandPx - 3f)
                    )
                    drawCircle(
                        color = TextPrimary.copy(alpha = 0.09f),
                        radius = outerPx,
                        center = centre,
                        style = Stroke(width = 1.5f)
                    )

                    for (sector in 0 until sectors) {
                        val angle = Math.toRadians((sector * sectorAngle - 90f).toDouble())
                        drawLine(
                            color = BackgroundBase.copy(alpha = 0.6f),
                            start = Offset(
                                centre.x + cos(angle).toFloat() * innerPx,
                                centre.y + sin(angle).toFloat() * innerPx
                            ),
                            end = Offset(
                                centre.x + cos(angle).toFloat() * outerPx,
                                centre.y + sin(angle).toFloat() * outerPx
                            ),
                            strokeWidth = 1.5f
                        )
                    }

                    val decoy = decoySectors.getOrElse(ring) { -1 }
                    if (decoy >= 0) {
                        val decoyStart = decoy * sectorAngle - 90f - sectorAngle * 0.3f
                        drawArc(
                            color = DormantStone.copy(alpha = 0.5f),
                            startAngle = decoyStart,
                            sweepAngle = sectorAngle * 0.6f,
                            useCenter = false,
                            topLeft = Offset(centre.x - midPx, centre.y - midPx),
                            size = Size(midPx * 2f, midPx * 2f),
                            style = Stroke(width = 3f)
                        )
                    }

                    val rotation = animatedAngles[ring].value
                    val channelStart = rotation - 90f - sectorAngle * 0.36f
                    val channelColor = when {
                        ring == flashRing && backlash -> BacklashRed
                        ring == flashRing -> GoldHighlight
                        locked -> AccentGold
                        live -> StormBlue
                        else -> DormantStone
                    }
                    if (live || locked) {
                        drawArc(
                            color = channelColor.copy(alpha = 0.3f),
                            startAngle = channelStart,
                            sweepAngle = sectorAngle * 0.72f,
                            useCenter = false,
                            topLeft = Offset(centre.x - midPx, centre.y - midPx),
                            size = Size(midPx * 2f, midPx * 2f),
                            style = Stroke(width = bandPx * 0.62f)
                        )
                    }
                    drawArc(
                        color = channelColor,
                        startAngle = channelStart,
                        sweepAngle = sectorAngle * 0.72f,
                        useCenter = false,
                        topLeft = Offset(centre.x - midPx, centre.y - midPx),
                        size = Size(midPx * 2f, midPx * 2f),
                        style = Stroke(width = bandPx * 0.3f)
                    )

                    if (previewLevel >= 1 && live) {
                        val previewStart = channelStart + sectorAngle
                        drawArc(
                            color = StormBlue.copy(alpha = 0.35f),
                            startAngle = previewStart,
                            sweepAngle = sectorAngle * 0.72f,
                            useCenter = false,
                            topLeft = Offset(centre.x - midPx, centre.y - midPx),
                            size = Size(midPx * 2f, midPx * 2f),
                            style = Stroke(width = bandPx * 0.18f)
                        )
                    }
                }

                drawCircle(
                    color = if (solved) AccentGold.copy(alpha = 0.25f) else StormBlue.copy(alpha = 0.18f),
                    radius = corePx,
                    center = centre
                )
            }

            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.cd_storm_core),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(64.dp)
                    .scale(corePulse)
            )

            repeat(gateNodes.coerceAtLeast(1)) { node ->
                val nodeAngle = if (node == 0) -90.0 else 90.0
                val nodeRadians = Math.toRadians(nodeAngle)
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_gate),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(46.dp)
                        .offset(
                            x = polar(outerRadius - 6.dp, cos(nodeRadians).toFloat()),
                            y = polar(outerRadius - 6.dp, sin(nodeRadians).toFloat())
                        )
                        .alpha(if (solved) gateGlow else 0.55f)
                )
            }

            for (ring in 0 until lockedCount.coerceAtMost(rings)) {
                val clampRadius = coreRadius + band * (ring + 1f) - 6.dp
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_forge_tool),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(22.dp)
                        .offset(
                            x = polar(clampRadius, 0.707f),
                            y = polar(clampRadius, -0.707f)
                        )
                        .alpha(0.9f)
                )
            }
        }
    }
}

private fun polar(radius: Dp, factor: Float): Dp = radius * factor
