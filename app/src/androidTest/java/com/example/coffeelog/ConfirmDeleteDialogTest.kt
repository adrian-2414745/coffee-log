package com.example.coffeelog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.coffeelog.ui.components.ConfirmDeleteDialog
import com.example.coffeelog.ui.components.RatingStars
import com.example.coffeelog.ui.theme.CoffeeLogTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ConfirmDeleteDialogTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun showsPrompt_and_yesConfirms() {
        var confirmed = false
        var dismissed = false
        rule.setContent {
            CoffeeLogTheme {
                ConfirmDeleteDialog(onConfirm = { confirmed = true }, onDismiss = { dismissed = true })
            }
        }
        rule.onNodeWithText("Are you sure you want to delete?").assertIsDisplayed()
        rule.onNodeWithText("Yes").performClick()
        assertTrue(confirmed)
        assertFalse(dismissed)
    }

    @Test
    fun cancel_dismissesWithoutConfirming() {
        var confirmed = false
        var dismissed = false
        rule.setContent {
            CoffeeLogTheme {
                ConfirmDeleteDialog(onConfirm = { confirmed = true }, onDismiss = { dismissed = true })
            }
        }
        rule.onNodeWithText("Cancel").performClick()
        assertTrue(dismissed)
        assertFalse(confirmed)
    }

    @Test
    fun ratingStars_tapSetsRating() {
        var rating: Int? = null
        rule.setContent {
            CoffeeLogTheme {
                RatingStars(rating = rating, editable = true, onRatingChange = { rating = it })
            }
        }
        // five star glyphs; tapping the fourth sets rating = 4
        rule.onAllNodesWithText("★")[3].performClick()
        assertEquals(4, rating)
    }
}
