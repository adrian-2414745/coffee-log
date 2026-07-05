package io.github.adrian2414745.coffeelog

import io.github.adrian2414745.coffeelog.util.RatioFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

class RatioFormatterTest {

    @Test
    fun formatsOneDecimal() {
        assertEquals("1:2.0", RatioFormatter.format(18.0, 36.0))
        assertEquals("1:2.2", RatioFormatter.format(18.0, 40.1))
        assertEquals("1:1.9", RatioFormatter.format(17.5, 34.0))
    }

    @Test
    fun roundsToOneDecimal() {
        assertEquals("1:2.0", RatioFormatter.format(18.0, 36.5)) // 2.027 -> 2.0
        assertEquals("1:2.1", RatioFormatter.format(18.0, 37.5)) // 2.083 -> 2.1
    }

    @Test
    fun placeholderForMissingOrZeroDose() {
        assertEquals("—", RatioFormatter.format(null, 36.0))
        assertEquals("—", RatioFormatter.format(18.0, null))
        assertEquals("—", RatioFormatter.format(0.0, 36.0))
        assertEquals("—", RatioFormatter.format(-1.0, 36.0))
    }
}
