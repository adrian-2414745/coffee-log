package com.example.coffeelog.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeelog.ui.theme.Archivo
import com.example.coffeelog.ui.theme.JetBrainsMono

/** Read-only auto-calculated ratio pill: dashed border, accent value, muted `AUTO` label. */
@Composable
fun RatioPill(ratio: String, modifier: Modifier = Modifier) {
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f)
    Row(
        modifier
            .width(126.dp)
            .height(34.dp)
            .drawBehind {
                val stroke = Stroke(
                    width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                        0f,
                    ),
                )
                val r = 5.dp.toPx()
                drawRoundRect(
                    color = borderColor,
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(r, r),
                    style = stroke,
                )
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = ratio,
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.tertiary,
        )
        Text(
            text = "AUTO",
            fontFamily = Archivo,
            fontWeight = FontWeight.W700,
            fontSize = 7.sp,
            letterSpacing = 0.9.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
        )
    }
}
