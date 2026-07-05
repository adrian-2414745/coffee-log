package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import io.github.adrian2414745.coffeelog.ui.theme.AppType

/** Inset-panel text field used for the coffee name (single line) and brew notes (multi-line). */
@Composable
fun InsetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    mono: Boolean = false,
) {
    val accent = MaterialTheme.colorScheme.tertiary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val selectionColors = TextSelectionColors(
        handleColor = accent,
        backgroundColor = accent.copy(alpha = 0.3f),
    )
    val textStyle = if (mono) {
        AppType.MonoField.copy(color = onSurface)
    } else {
        AppType.RowName.copy(color = onSurface)
    }
    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            singleLine = singleLine,
            textStyle = textStyle,
            cursorBrush = SolidColor(accent),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            decorationBox = { inner ->
                Box(Modifier.padding(horizontal = 14.dp, vertical = 13.dp)) {
                    if (value.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = onSurface.copy(alpha = 0.4f)),
                        )
                    }
                    inner()
                }
            },
        )
    }
}
