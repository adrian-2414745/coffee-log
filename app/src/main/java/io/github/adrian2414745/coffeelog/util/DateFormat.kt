package io.github.adrian2414745.coffeelog.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Brew card date, e.g. `27 JUN 2026`. */
object DateFormat {
    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)

    fun brewDate(epochMillis: Long): String =
        formatter.format(Date(epochMillis)).uppercase(Locale.US)
}
