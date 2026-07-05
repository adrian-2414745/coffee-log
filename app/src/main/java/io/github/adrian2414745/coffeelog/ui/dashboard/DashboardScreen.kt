package io.github.adrian2414745.coffeelog.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.adrian2414745.coffeelog.data.db.DashboardRow
import io.github.adrian2414745.coffeelog.ui.components.ConfirmDeleteDialog
import io.github.adrian2414745.coffeelog.ui.components.AddBar
import io.github.adrian2414745.coffeelog.ui.components.MetricCell
import io.github.adrian2414745.coffeelog.ui.components.MetricGrid
import io.github.adrian2414745.coffeelog.ui.theme.AppType
import io.github.adrian2414745.coffeelog.ui.theme.Emphasis
import io.github.adrian2414745.coffeelog.util.MetricFormat

@Composable
fun DashboardScreen(
    onCoffeeClick: (Long) -> Unit,
    onAddCoffee: () -> Unit,
    onSettings: () -> Unit,
    viewModel: DashboardViewModel = viewModel(factory = DashboardViewModel.Factory),
) {
    val rows by viewModel.rows.collectAsStateWithLifecycle()
    var pendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { AddBar(label = "ADD COFFEE", onClick = onAddCoffee) },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            Row(
                Modifier.fillMaxWidth().padding(start = 20.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Coffee Log",
                    style = AppType.DashboardTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                IconButton(onClick = onSettings) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings", modifier = Modifier.padding(2.dp))
                }
            }
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                items(rows, key = { it.id }) { row ->
                    CoffeeRow(
                        row = row,
                        onClick = { onCoffeeClick(row.id) },
                        onLongPress = { pendingDelete = row.id },
                    )
                }
            }
        }
    }

    pendingDelete?.let { id ->
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.deleteCoffee(id)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun CoffeeRow(
    row: DashboardRow,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
) {
    val shape = RoundedCornerShape(6.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), shape)
            .combinedClickable(onClick = onClick, onLongClick = onLongPress)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val lowEmphasis = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            Text(
                text = buildAnnotatedString {
                    append(row.name)
                    row.roastLevel?.takeIf { it.isNotBlank() }?.let { roast ->
                        append(" ")
                        withStyle(AppType.HelperSubtitle.toSpanStyle().copy(color = lowEmphasis)) {
                            append("(${roast.lowercase()})")
                        }
                    }
                },
                style = AppType.RowName,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp),
            )
            Text(
                text = "›",
                style = AppType.Chevron,
                color = Emphasis.tertiary,
            )
        }
        if (row.favBrewId != null) {
            MetricGrid(
                cells = listOf(
                    MetricCell("DOSE", MetricFormat.weight(row.favGroundsWeightG)),
                    MetricCell("GRIND", MetricFormat.grind(row.favGrindSize), muted = row.favGrindSize == null),
                    MetricCell(
                        "TIME",
                        MetricFormat.seconds(row.favBrewTimeSec),
                        muted = row.favBrewTimeSec == null,
                    ),
                    MetricCell("YIELD", MetricFormat.weight(row.favLiquidWeightG)),
                ),
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}
