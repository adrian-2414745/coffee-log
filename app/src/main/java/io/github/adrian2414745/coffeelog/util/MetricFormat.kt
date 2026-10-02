package io.github.adrian2414745.coffeelog.util

import java.util.Locale

/** Shared formatting for the numeric metric values shown on cards and the dashboard. */
object MetricFormat {
    const val DASH = "—"

    /** Dose / yield: always one decimal (e.g. 18.0). */
    fun weight(v: Double?): String =
        if (v == null) DASH else String.format(Locale.US, "%.1f", v)

    /** Grind: up to two decimals, at least one, e.g. 5.0, 5.1, 5.25. */
    fun grind(v: Double?): String = v?.let(::grindValue) ?: DASH

    fun grindValue(v: Double): String {
        val s = String.format(Locale.US, "%.2f", v)
        return if (s.endsWith("0")) s.dropLast(1) else s
    }

    fun seconds(v: Int?): String = v?.toString() ?: DASH

    /** Temperature / volume: integer when whole, otherwise one decimal. */
    fun plain(v: Double?): String {
        if (v == null) return DASH
        return if (v % 1.0 == 0.0) v.toLong().toString() else String.format(Locale.US, "%.1f", v)
    }

    fun score(rating: Int?): String = rating?.toString() ?: DASH
}
