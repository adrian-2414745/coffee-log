package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.adrian2414745.coffeelog.ui.theme.Archivo

/**
 * Row of five stars. Filled stars use the accent color, empty are muted.
 * When [editable], tapping a star sets the rating (tapping the current top star clears it).
 */
@Composable
fun RatingStars(
    rating: Int?,
    editable: Boolean,
    modifier: Modifier = Modifier,
    onRatingChange: (Int?) -> Unit = {},
) {
    val filled = rating ?: 0
    val accent = MaterialTheme.colorScheme.tertiary
    val muted = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)
    Row(modifier) {
        for (star in 1..5) {
            val interaction = remember { MutableInteractionSource() }
            Text(
                text = "★",
                fontFamily = Archivo,
                fontWeight = FontWeight.W400,
                fontSize = 18.sp,
                color = if (star <= filled) accent else muted,
                modifier = Modifier
                    .then(
                        if (editable) {
                            Modifier.clickable(
                                interactionSource = interaction,
                                indication = null,
                            ) { onRatingChange(if (rating == star) null else star) }
                        } else {
                            Modifier
                        },
                    )
                    .padding(horizontal = 3.dp),
            )
        }
    }
}
