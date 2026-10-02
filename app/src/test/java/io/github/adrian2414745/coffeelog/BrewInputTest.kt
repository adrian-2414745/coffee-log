package io.github.adrian2414745.coffeelog

import io.github.adrian2414745.coffeelog.ui.brewedit.StepField
import io.github.adrian2414745.coffeelog.ui.brewedit.formatStepValue
import io.github.adrian2414745.coffeelog.ui.brewedit.sanitizeNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class BrewInputTest {

    @Test
    fun sanitize_capsDecimals() {
        assertEquals("5.25", sanitizeNumber("5.257", true, maxDecimals = 2))
        assertEquals("5.2", sanitizeNumber("5.2", true, maxDecimals = 2))
        assertEquals("5.", sanitizeNumber("5.", true, maxDecimals = 2))
        assertEquals("5", sanitizeNumber("5", true, maxDecimals = 2))
    }

    @Test
    fun sanitize_keepsFirstDotAndDigitsOnly() {
        assertEquals("5.25", sanitizeNumber("5.2.5x", true, maxDecimals = 2))
        assertEquals("18.125", sanitizeNumber("18.125", true))
        assertEquals("525", sanitizeNumber("5.25", false))
    }

    @Test
    fun grindStep_isFiveHundredths() {
        assertEquals(0.05, StepField.GRIND.step, 0.0)
        assertEquals("5.15", formatStepValue(StepField.GRIND, 5.1 + StepField.GRIND.step))
        assertEquals("5.2", formatStepValue(StepField.GRIND, 5.15 + StepField.GRIND.step))
        assertEquals("5.3", formatStepValue(StepField.GRIND, 5.25 + StepField.GRIND.step))
    }

    @Test
    fun otherSteps_keepFixedDecimals() {
        assertEquals("18.5", formatStepValue(StepField.DOSE, 18.5))
        assertEquals("18.0", formatStepValue(StepField.DOSE, 18.0))
        assertEquals("30", formatStepValue(StepField.TIME, 30.0))
    }
}
