package com.example.coffeelog.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeelog.data.db.BrewEntity
import com.example.coffeelog.ui.components.ConfirmDeleteDialog
import com.example.coffeelog.ui.components.CoffeeFab
import com.example.coffeelog.ui.components.ScreenHeader

@Composable
fun HistoryScreen(
    coffeeId: Long,
    onBack: () -> Unit,
    onAddBrew: () -> Unit,
    onEditBrew: (Long) -> Unit,
    onEditCoffee: () -> Unit,
    viewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory),
) {
    val name by viewModel.coffeeName.collectAsStateWithLifecycle()
    val brews by viewModel.brews.collectAsStateWithLifecycle()
    var pendingDelete by remember { mutableStateOf<BrewEntity?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = { CoffeeFab(onClick = onAddBrew, contentDescription = "Add brew") },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            ScreenHeader(
                title = name,
                onBack = onBack,
                actions = {
                    IconButton(onClick = onEditCoffee) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit coffee",
                            modifier = Modifier.padding(2.dp),
                        )
                    }
                },
            )
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp),
            ) {
                items(brews, key = { it.id }) { brew ->
                    BrewCard(
                        brew = brew,
                        onClick = { onEditBrew(brew.id) },
                        onLongPress = { pendingDelete = brew },
                    )
                }
            }
        }
    }

    pendingDelete?.let { brew ->
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.deleteBrew(brew)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }
}
