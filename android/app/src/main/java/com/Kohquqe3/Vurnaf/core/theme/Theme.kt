package com.Kohquqe3.Vurnaf.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

const val PRESET_NAME = "CASINO_GOLD"

private val ForgeColorScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = OnGold,
    primaryContainer = SurfaceWarm,
    onPrimaryContainer = GoldHighlight,
    secondary = StormBlue,
    onSecondary = OnGold,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = TextPrimary,
    tertiary = Patina,
    onTertiary = OnGold,
    background = BackgroundBase,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = Bronze,
    outlineVariant = BronzeShade,
    error = BacklashRed,
    onError = TextPrimary,
    scrim = BackgroundBase
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ForgeColorScheme,
        typography = AppTypography,
        content = content
    )
}
