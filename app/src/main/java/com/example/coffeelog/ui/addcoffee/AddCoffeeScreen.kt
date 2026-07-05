package com.example.coffeelog.ui.addcoffee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeelog.ui.components.FieldLabel
import com.example.coffeelog.ui.components.InsetTextField
import com.example.coffeelog.ui.components.SaveCancelBar
import com.example.coffeelog.ui.components.ScreenHeader
import com.example.coffeelog.ui.components.SegmentedField

@Composable
fun AddCoffeeScreen(
    onDone: () -> Unit,
    viewModel: AddCoffeeViewModel = viewModel(factory = AddCoffeeViewModel.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            SaveCancelBar(
                saveLabel = "ADD TO INDEX",
                onSave = { viewModel.save(onDone) },
                onCancel = onDone,
                saveEnabled = state.canSave,
            )
        },
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner),
        ) {
            ScreenHeader(title = "Add Coffee", onBack = onDone)
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    FieldLabel("NAME")
                    InsetTextField(
                        value = state.name,
                        onValueChange = viewModel::onNameChange,
                        placeholder = "e.g. Ethiopia Guji",
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    FieldLabel("ROAST LEVEL")
                    SegmentedField(
                        options = listOf("LIGHT", "MEDIUM", "DARK"),
                        selected = state.roastLevel,
                        onSelect = viewModel::onRoastChange,
                    )
                }
            }
        }
    }
}
