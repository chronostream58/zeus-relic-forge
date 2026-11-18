package com.Kohquqe3.Vurnaf.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.Kohquqe3.Vurnaf.core.theme.Bronze
import com.Kohquqe3.Vurnaf.core.theme.StormBlue
import com.Kohquqe3.Vurnaf.core.theme.SurfaceRaised
import com.Kohquqe3.Vurnaf.core.theme.SurfaceWarm
import com.Kohquqe3.Vurnaf.core.theme.TextPrimary
import com.Kohquqe3.Vurnaf.core.theme.TextSecondary
import com.Kohquqe3.Vurnaf.core.ui.ScreenBackground

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
) {
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent(modifier = modifier)
}

@Composable
private fun SplashContent(modifier: Modifier = Modifier) {
    val overlay = Brush.verticalGradient(
        listOf(
            BackgroundBase.copy(alpha = 0.95f),
            BackgroundBase.copy(alpha = 0.88f),
            SurfaceWarm.copy(alpha = 0.95f)
        )
    )
    val transition = rememberInfiniteTransition()
    val outerSpin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing))
    )
    val middleSpin by transition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing))
    )
    val innerSpin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing))
    )
    val logoAlpha by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400), repeatMode = RepeatMode.Reverse)
    )
    val moteDrift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing))
    )
    val ruleWidth by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), repeatMode = RepeatMode.Reverse)
    )

    ScreenBackground(overlay = overlay, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val baseY = size.height * 0.78f
            repeat(2) { index ->
                val travel = ((moteDrift + index * 0.5f) % 1f)
                drawCircle(
                    color = Bronze.copy(alpha = (1f - travel) * 0.55f),
                    radius = 3.5f + index * 1.5f,
                    center = Offset(
                        x = size.width * (0.28f + index * 0.44f),
                        y = baseY - travel * size.height * 0.42f
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(180.dp)
                    .height(80.dp)
                    .alpha(logoAlpha)
            )

            Spacer(modifier = Modifier.height(26.dp))

            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(228.dp)) {
                    val strokes = listOf(
                        Triple(outerSpin, 1f, 0.34f),
                        Triple(middleSpin, 0.82f, 0.26f),
                        Triple(innerSpin, 0.64f, 0.2f)
                    )
                    strokes.forEach { (spin, factor, alpha) ->
                        val inset = size.minDimension * (1f - factor) / 2f
                        rotate(degrees = spin) {
                            drawArc(
                                color = StormBlue.copy(alpha = alpha),
                                startAngle = 18f,
                                sweepAngle = 112f,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = Size(size.width - inset * 2f, size.height - inset * 2f),
                                style = Stroke(width = 5f)
                            )
                            drawArc(
                                color = AccentGold.copy(alpha = alpha * 0.8f),
                                startAngle = 198f,
                                sweepAngle = 92f,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = Size(size.width - inset * 2f, size.height - inset * 2f),
                                style = Stroke(width = 5f)
                            )
                        }
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = SurfaceRaised.copy(alpha = 0.9f),
                    border = BorderStroke(2.dp, Bronze),
                    modifier = Modifier.size(160.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = stringResource(R.string.cd_storm_core),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(132.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f * ruleWidth)
                    .height(2.dp)
                    .background(AccentGold, RoundedCornerShape(1.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                color = AccentGold,
                trackColor = SurfaceRaised,
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
