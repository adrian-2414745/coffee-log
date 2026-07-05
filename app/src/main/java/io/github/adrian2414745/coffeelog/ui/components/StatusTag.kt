package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.adrian2414745.coffeelog.ui.theme.AppType

/** Read-only status chip: DISP / LEVEL / ★ FAV, active (accent) or muted. */
@Composable
fun StatusTag(text: String, active: Boolean, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(4.dp)
    val accent = MaterialTheme.colorScheme.tertiary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val bg = if (active) accent.copy(alpha = 0.12f) else Color.Transparent
    val border = if (active) accent.copy(alpha = 0.42f) else onSurface.copy(alpha = 0.16f)
    val fg = if (active) accent else onSurface.copy(alpha = 0.28f)
    Text(
        text = text,
        style = AppType.StatusTag,
        color = fg,
        modifier = modifier
            .clip(shape)
            .background(bg)
            .border(BorderStroke(1.dp, border), shape)
            .padding(horizontal = 7.dp, vertical = 3.dp),
    )
}
