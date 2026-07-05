package com.example.coffeelog.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.example.coffeelog.R

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

val AppTypography = Typography(
    titleLarge = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W800),
    titleMedium = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W700),
    bodyLarge = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W400),
    bodyMedium = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W400),
    labelLarge = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W700),
    labelSmall = TextStyle(fontFamily = Archivo, fontWeight = FontWeight.W700),
)
