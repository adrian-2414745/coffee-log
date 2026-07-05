package com.example.coffeelog.ui.dashboard

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeelog.data.db.DashboardRow
import com.example.coffeelog.ui.components.ConfirmDeleteDialog
import com.example.coffeelog.ui.components.CoffeeFab
import com.example.coffeelog.ui.components.MetricCell
import com.example.coffeelog.ui.components.MetricGrid
import com.example.coffeelog.ui.theme.Archivo
import com.example.coffeelog.util.MetricFormat

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
        floatingActionButton = { CoffeeFab(onClick = onAddCoffee, contentDescription = "Add coffee") },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            Row(
                Modifier.fillMaxWidth().padding(start = 20.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Coffee Index",
                    fontFamily = Archivo,
                    fontWeight = FontWeight.W800,
                    fontSize = 22.sp,
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
                        withStyle(SpanStyle(color = lowEmphasis, fontWeight = FontWeight.W400)) {
                            append("(${roast.lowercase()})")
                        }
                    }
                },
                fontFamily = Archivo,
                fontWeight = FontWeight.W700,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp),
            )
            Text(
                text = "›",
                fontFamily = Archivo,
                fontWeight = FontWeight.W400,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
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
