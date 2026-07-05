package com.example.coffeelog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeelog.ui.theme.Archivo
import com.example.coffeelog.ui.theme.JetBrainsMono

/**
 * A metric input row: label on the left, and a `− value +` stepper on the right whose
 * centre value stays directly editable via the numeric keyboard.
 *
 * [value] is the raw string state (so partial input like `5.` is preserved).
 * [onStep] receives -1 or +1; the caller nudges the value.
 */
@Composable
fun StepperField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onStep: (Int) -> Unit,
    modifier: Modifier = Modifier,
    unit: String? = null,
    keyboardType: KeyboardType = KeyboardType.Decimal,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FieldLabel(label)
        Row(
            Modifier
                .width(150.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StepAffordance("−") { onStep(-1) }
            CellDivider()
            val onSurface = MaterialTheme.colorScheme.onSurface
            val accent = MaterialTheme.colorScheme.tertiary
            val selectionColors = TextSelectionColors(
                handleColor = accent,
                backgroundColor = accent.copy(alpha = 0.3f),
            )
            // Keep a TextFieldValue locally so we can select the whole value when the
            // field gains focus (tap → highlight → type replaces). External changes from
            // the −/+ steppers are reflected back into it.
            var fieldValue by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }
            if (fieldValue.text != value) {
                fieldValue = fieldValue.copy(text = value, selection = TextRange(value.length))
            }
            CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
                BasicTextField(
                    value = fieldValue,
                    onValueChange = { newValue ->
                        fieldValue = newValue
                        if (newValue.text != value) onValueChange(newValue.text)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                fieldValue = fieldValue.copy(selection = TextRange(0, fieldValue.text.length))
                            }
                        },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp,
                        color = onSurface,
                        textAlign = TextAlign.Center,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    cursorBrush = SolidColor(accent),
                    decorationBox = { inner ->
                        Row(
                            Modifier.fillMaxWidth().fillMaxHeight(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            inner()
                            if (unit != null) {
                                Text(
                                    text = unit,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 9.sp,
                                    color = onSurface.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(start = 1.dp),
                                )
                            }
                        }
                    },
                )
            }
            CellDivider()
            StepAffordance("+") { onStep(1) }
        }
    }
}

@Composable
internal fun FieldLabel(label: String) {
    Text(
        text = label,
        fontFamily = Archivo,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
    )
}

@Composable
private fun StepAffordance(symbol: String, onClick: () -> Unit) {
    Box(
        Modifier
            .width(34.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            fontFamily = Archivo,
            fontWeight = FontWeight.W400,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        )
    }
}

@Composable
private fun CellDivider() {
    Box(
        Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
    )
}
