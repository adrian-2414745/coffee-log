package com.example.coffeelog.util

import java.util.Locale

/**
 * The "golden ratio" of a brew: liquid yield weight over coffee dose, shown as `1:n`
 * with `n` rounded to one decimal. Volume is never used. Returns [PLACEHOLDER] when the
 * dose is missing or non-positive.
 */
object RatioFormatter {
    const val PLACEHOLDER = "—"

    fun format(groundsWeightG: Double?, liquidWeightG: Double?): String {
        if (groundsWeightG == null || liquidWeightG == null) return PLACEHOLDER
        if (groundsWeightG <= 0.0) return PLACEHOLDER
        val n = liquidWeightG / groundsWeightG
        return String.format(Locale.US, "1:%.1f", n)
    }
}
