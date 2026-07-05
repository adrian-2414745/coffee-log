package io.github.adrian2414745.coffeelog.ui.components

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
import androidx.compose.ui.unit.dp
import io.github.adrian2414745.coffeelog.ui.theme.AppType

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
            style = AppType.Value,
            color = MaterialTheme.colorScheme.tertiary,
        )
        Text(
            text = "AUTO",
            style = AppType.RatioPillLabel,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
        )
    }
}
