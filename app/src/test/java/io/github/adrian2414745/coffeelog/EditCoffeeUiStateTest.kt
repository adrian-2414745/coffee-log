package io.github.adrian2414745.coffeelog

import io.github.adrian2414745.coffeelog.ui.editcoffee.EditCoffeeUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EditCoffeeUiStateTest {

    @Test
    fun canSave_requiresNonBlankName() {
        assertTrue(EditCoffeeUiState(name = "Brazil").canSave)
        assertTrue(EditCoffeeUiState(name = "Brazil", roastLevel = "DARK").canSave)
    }

    @Test
    fun canSave_falseWhenNameBlank() {
        assertFalse(EditCoffeeUiState(name = "").canSave)
        assertFalse(EditCoffeeUiState(name = "   ").canSave)
    }
}
