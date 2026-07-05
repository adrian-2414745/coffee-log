package com.example.coffeelog.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeelog.data.db.BrewEntity
import com.example.coffeelog.ui.components.MetricCell
import com.example.coffeelog.ui.components.MetricGrid
import com.example.coffeelog.ui.components.StatusTag
import com.example.coffeelog.ui.theme.JetBrainsMono
import com.example.coffeelog.util.DateFormat
import com.example.coffeelog.util.MetricFormat
import com.example.coffeelog.util.RatioFormatter

private fun optional(label: String, value: String, isNull: Boolean, unit: String? = null): MetricCell =
    MetricCell(label, value, unit = if (isNull) null else unit, muted = isNull)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrewCard(
    brew: BrewEntity,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(6.dp)
    Column(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape)
            .combinedClickable(onClick = onClick, onLongClick = onLongPress)
            .padding(horizontal = 15.dp, vertical = 14.dp),
    ) {
        MetricGrid(
            cells = listOf(
                MetricCell("DOSE", MetricFormat.weight(brew.groundsWeightG), unit = "g"),
                optional("GRIND", MetricFormat.grind(brew.grindSize), brew.grindSize == null),
                optional("TIME", MetricFormat.seconds(brew.brewTimeSec), brew.brewTimeSec == null, unit = "s"),
                optional("TEMP", MetricFormat.plain(brew.waterTemp), brew.waterTemp == null, unit = "°"),
                MetricCell("YIELD", MetricFormat.weight(brew.liquidWeightG), unit = "g"),
                optional("VOL", MetricFormat.plain(brew.liquidVolumeMl), brew.liquidVolumeMl == null, unit = "ml"),
                MetricCell("RATIO", RatioFormatter.format(brew.groundsWeightG, brew.liquidWeightG), accent = true),
                optional("SCORE", MetricFormat.score(brew.rating), brew.rating == null, unit = "/5"),
            ),
        )
        Row(
            Modifier.fillMaxWidth().padding(top = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                StatusTag("DISP", active = brew.usedDispenser)
                StatusTag("LEVEL", active = brew.usedLeveler)
                StatusTag("★ FAV", active = brew.isFavorite)
            }
            Text(
                text = DateFormat.brewDate(brew.createdAt),
                fontFamily = JetBrainsMono,
                fontWeight = FontWeight.W500,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            )
        }
    }
}
