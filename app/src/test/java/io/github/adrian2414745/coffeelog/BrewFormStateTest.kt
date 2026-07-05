package io.github.adrian2414745.coffeelog

import io.github.adrian2414745.coffeelog.ui.brewedit.BrewFormState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BrewFormStateTest {

    @Test
    fun canSave_requiresPositiveDoseAndYield() {
        assertTrue(BrewFormState(dose = "18", yieldG = "36").canSave)
        assertTrue(BrewFormState(dose = "18.0", yieldG = "36.5").canSave)
    }

    @Test
    fun canSave_falseWhenMandatoryMissingOrZero() {
        assertFalse(BrewFormState(dose = "", yieldG = "36").canSave)
        assertFalse(BrewFormState(dose = "18", yieldG = "").canSave)
        assertFalse(BrewFormState(dose = "0", yieldG = "36").canSave)
        assertFalse(BrewFormState(dose = "18", yieldG = "0").canSave)
        assertFalse(BrewFormState(dose = "abc", yieldG = "36").canSave)
    }

    @Test
    fun canSave_ignoresOptionalFields() {
        // no grind/time/temp/vol/rating provided, still saveable
        assertTrue(BrewFormState(dose = "18", yieldG = "36", grind = "", time = "", temp = "", vol = "").canSave)
    }

    @Test
    fun ratio_isLiveFromDoseAndYield() {
        assertEquals("1:2.0", BrewFormState(dose = "18", yieldG = "36").ratio)
        assertEquals("1:2.2", BrewFormState(dose = "18", yieldG = "40.1").ratio)
        assertEquals("—", BrewFormState(dose = "", yieldG = "36").ratio)
    }
}
