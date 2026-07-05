package io.github.adrian2414745.coffeelog.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ---- Palette B — Functionalist (light) ----
private val LightBackground = Color(0xFFEFEDE7)
private val LightSurface = Color(0xFFEFEDE7)
private val LightCard = Color(0xFFFAF9F5)          // surfaceContainer
private val LightInset = Color(0xFFE9E5DC)          // surfaceContainerHighest
private val LightBorder = Color(0xFFE8E4DA)         // outlineVariant
private val LightOnSurface = Color(0xFF2B2A27)
private val LightAccent = Color(0xFFAE3B36)         // red — tertiary

// ---- Palette A — Graphite (dark) ----
private val DarkBackground = Color(0xFF201E1B)
private val DarkSurface = Color(0xFF201E1B)
private val DarkCard = Color(0xFF2A2723)            // surfaceContainer
private val DarkInset = Color(0xFF211E1A)           // surfaceContainerHighest
private val DarkBorder = Color(0xFF393530)          // outlineVariant
private val DarkOnSurface = Color(0xFFEDE7DB)
private val DarkAccent = Color(0xFFE9B10A)          // amber — tertiary

// Shared amber accent (FAB, SAVE)
val Amber = Color(0xFFE9B10A)

val LightColors: ColorScheme = lightColorScheme(
    primary = Amber,
    onPrimary = Color(0xFF2B2A27),
    secondary = LightOnSurface,
    onSecondary = LightBackground,
    tertiary = LightAccent,
    onTertiary = Color(0xFFFAF9F5),
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceContainer = LightCard,
    surfaceContainerHigh = LightCard,
    surfaceContainerHighest = LightInset,
    surfaceVariant = LightInset,
    onSurfaceVariant = LightOnSurface,
    outline = LightBorder,
    outlineVariant = LightBorder,
    error = LightAccent,
)

val DarkColors: ColorScheme = darkColorScheme(
    primary = Amber,
    onPrimary = Color(0xFF201E1B),
    secondary = DarkOnSurface,
    onSecondary = DarkBackground,
    tertiary = DarkAccent,
    onTertiary = Color(0xFF201E1B),
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceContainer = DarkCard,
    surfaceContainerHigh = DarkCard,
    surfaceContainerHighest = DarkInset,
    surfaceVariant = DarkInset,
    onSurfaceVariant = DarkOnSurface,
    outline = DarkBorder,
    outlineVariant = DarkBorder,
    error = DarkAccent,
)
