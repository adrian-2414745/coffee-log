package io.github.adrian2414745.coffeelog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.adrian2414745.coffeelog.R

@OptIn(ExperimentalTextApi::class)
private fun archivo(weight: Int) = Font(
    R.font.archivo,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

@OptIn(ExperimentalTextApi::class)
private fun mono(weight: Int) = Font(
    R.font.jetbrains_mono,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

// Archivo — titles, labels, buttons.
val Archivo = FontFamily(
    archivo(400), archivo(500), archivo(600), archivo(700), archivo(800), archivo(900),
)

// JetBrains Mono — all numeric values.
val JetBrainsMono = FontFamily(
    mono(400), mono(500), mono(700),
)

private fun shape(family: FontFamily, weight: FontWeight, size: Int, tracking: Double = 0.0) =
    TextStyle(fontFamily = family, fontWeight = weight, fontSize = size.sp, letterSpacing = tracking.sp)

/**
 * The named text roles from docs/design-style.md → "Type scale (as used)". Each value is
 * **typography only** — family, weight, size and tracking — so color/emphasis is applied at
 * the call site (see [Emphasis]). Use these instead of hand-writing
 * `fontFamily`/`fontWeight`/`fontSize`/`letterSpacing`, so the scale stays consistent and the
 * doc's table is enforceable in code. Add a role here before introducing a new size/weight.
 */
object AppType {
    // Titles & names — Archivo
    val DashboardTitle = shape(Archivo, FontWeight.W800, 22)
    val ScreenTitle = shape(Archivo, FontWeight.W800, 18)
    val RowName = shape(Archivo, FontWeight.W700, 15) // card / row name, name text-field input
    val SettingTitle = shape(Archivo, FontWeight.W700, 14)
    val FieldLabel = shape(Archivo, FontWeight.W700, 14) // color = Emphasis.emphasis

    // Tracked uppercase labels — Archivo
    val SectionLabel = shape(Archivo, FontWeight.W700, 9, 1.0)
    val MetricLabel = shape(Archivo, FontWeight.W700, 9, 0.5)
    val StatusTag = shape(Archivo, FontWeight.W700, 8, 0.7)
    val RatioPillLabel = shape(Archivo, FontWeight.W700, 7, 0.9)
    val SegmentedOption = shape(Archivo, FontWeight.W700, 11, 0.6)

    // Buttons — Archivo
    val PrimaryButton = shape(Archivo, FontWeight.W800, 13, 0.9)
    val CancelButton = shape(Archivo, FontWeight.W800, 12, 0.6)

    // Numeric & metadata — Mono
    val HeaderSubtitle = shape(JetBrainsMono, FontWeight.W500, 10, 0.8)
    val HelperSubtitle = shape(JetBrainsMono, FontWeight.W400, 10) // descriptor / muted subtitle
    val MetricValue = shape(JetBrainsMono, FontWeight.W500, 15)
    val Value = shape(JetBrainsMono, FontWeight.W500, 14) // ratio-pill / stepper value
    val MonoField = shape(JetBrainsMono, FontWeight.W400, 13) // notes body, numeric field input
    val UnitSuffix = shape(JetBrainsMono, FontWeight.W500, 9)
    val DateStamp = shape(JetBrainsMono, FontWeight.W500, 11)

    // Glyphs — Archivo
    val StepperGlyph = shape(Archivo, FontWeight.W400, 17)
    val Back = shape(Archivo, FontWeight.W400, 26)
    val Chevron = shape(Archivo, FontWeight.W400, 18)
}

/**
 * The neutral on-surface emphasis ramp from docs/design-style.md → "Emphasis levels". Prefer
 * these named steps over ad-hoc `onSurface.copy(alpha = …)`. Roles whose alpha sits between
 * steps (e.g. header/section subtitles at .45, CANCEL at .6, RATIO label at .35) still set
 * their color explicitly at the call site.
 */
object Emphasis {
    val primary: Color @Composable get() = MaterialTheme.colorScheme.onSurface
    val emphasis: Color @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
    val secondary: Color @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val tertiary: Color @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    val disabled: Color @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)
    val divider: Color @Composable get() = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
}

val AppTypography = Typography(
    titleLarge = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W800),
    titleMedium = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W700),
    bodyLarge = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W400),
    bodyMedium = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W400),
    labelLarge = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W700),
    labelSmall = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W700),
)
