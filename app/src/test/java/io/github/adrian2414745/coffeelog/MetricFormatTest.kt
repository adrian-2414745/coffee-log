package io.github.adrian2414745.coffeelog

import io.github.adrian2414745.coffeelog.util.MetricFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class MetricFormatTest {

    @Test
    fun grind_keepsTwoDecimals() {
        assertEquals("5.25", MetricFormat.grind(5.25))
        assertEquals("5.15", MetricFormat.grind(5.1 + 0.05)) // float noise 5.1499…
    }

    @Test
    fun grind_trimsToAtLeastOneDecimal() {
        assertEquals("5.1", MetricFormat.grind(5.1))
        assertEquals("5.0", MetricFormat.grind(5.0))
        assertEquals("12.5", MetricFormat.grind(12.5))
    }

    @Test
    fun grind_roundsBeyondTwoDecimals() {
        assertEquals("5.13", MetricFormat.grind(5.126))
    }

    @Test
    fun grind_dashWhenMissing() {
        assertEquals(MetricFormat.DASH, MetricFormat.grind(null))
    }
}
