package io.github.adrian2414745.coffeelog.util

import java.util.Locale

/** Shared formatting for the numeric metric values shown on cards and the dashboard. */
object MetricFormat {
    const val DASH = "—"

    /** Dose / yield: always one decimal (e.g. 18.0). */
    fun weight(v: Double?): String =
        if (v == null) DASH else String.format(Locale.US, "%.1f", v)

    /** Grind: one decimal, e.g. 5.1. */
    fun grind(v: Double?): String =
        if (v == null) DASH else String.format(Locale.US, "%.1f", v)

    fun seconds(v: Int?): String = v?.toString() ?: DASH

    /** Temperature / volume: integer when whole, otherwise one decimal. */
    fun plain(v: Double?): String {
        if (v == null) return DASH
        return if (v % 1.0 == 0.0) v.toLong().toString() else String.format(Locale.US, "%.1f", v)
    }

    fun score(rating: Int?): String = rating?.toString() ?: DASH
}
