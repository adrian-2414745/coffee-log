package io.github.adrian2414745.coffeelog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.adrian2414745.coffeelog.ui.theme.AppType
import io.github.adrian2414745.coffeelog.ui.theme.Emphasis

data class MetricCell(
    val label: String,
    val value: String,
    val unit: String? = null,
    val accent: Boolean = false,
    val muted: Boolean = false,
)

/**
 * The recurring 4-column inset metric grid. Cells are laid out four-per-row; pass 4
 * cells for the dashboard favorite summary or 8 for a two-row brew card.
 */
@Composable
fun MetricGrid(cells: List<MetricCell>, modifier: Modifier = Modifier) {
    val rows = cells.chunked(4)
    val divider = Emphasis.divider
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        rows.forEachIndexed { rowIndex, row ->
            Row(Modifier.height(IntrinsicSize.Min).fillMaxWidth()) {
                for (i in 0 until 4) {
                    val cell = row.getOrNull(i)
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                    ) {
                        if (cell != null) MetricCellContent(cell)
                    }
                    if (i < 3) {
                        androidx.compose.foundation.layout.Box(
                            Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(divider),
                        )
                    }
                }
            }
            if (rowIndex < rows.size - 1) {
                androidx.compose.foundation.layout.Box(
                    Modifier.fillMaxWidth().height(1.dp).background(divider),
                )
            }
        }
    }
}

@Composable
private fun MetricCellContent(cell: MetricCell) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Text(
        text = cell.label,
        style = AppType.MetricLabel,
        color = Emphasis.secondary,
    )
    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 3.dp)) {
        val valueColor = when {
            cell.accent -> MaterialTheme.colorScheme.tertiary
            cell.muted -> onSurface.copy(alpha = 0.4f)
            else -> onSurface
        }
        Text(
            text = cell.value,
            style = AppType.MetricValue,
            color = valueColor,
        )
        if (cell.unit != null) {
            Text(
                text = cell.unit,
                style = AppType.UnitSuffix,
                color = Emphasis.tertiary,
                modifier = Modifier.padding(start = 1.dp, bottom = 1.dp),
            )
        }
    }
}
