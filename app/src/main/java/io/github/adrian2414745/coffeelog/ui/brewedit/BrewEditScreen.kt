package io.github.adrian2414745.coffeelog.ui.brewedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.adrian2414745.coffeelog.ui.components.FieldLabel
import io.github.adrian2414745.coffeelog.ui.components.InsetTextField
import io.github.adrian2414745.coffeelog.ui.components.RatingStars
import io.github.adrian2414745.coffeelog.ui.components.RatioPill
import io.github.adrian2414745.coffeelog.ui.components.SaveCancelBar
import io.github.adrian2414745.coffeelog.ui.components.ScreenHeader
import io.github.adrian2414745.coffeelog.ui.components.StepperField

@Composable
fun BrewEditScreen(
    coffeeId: Long,
    brewId: Long?,
    onDone: () -> Unit,
    viewModel: BrewEditViewModel = viewModel(factory = BrewEditViewModel.Factory),
) {
    val name by viewModel.coffeeName.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()

    val subtitle = if (viewModel.isEdit) {
        name.uppercase()
    } else {
        "${name.uppercase()} · UNSAVED"
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            SaveCancelBar(
                saveLabel = "SAVE",
                onSave = { viewModel.save(onDone) },
                onCancel = onDone,
                saveEnabled = form.canSave,
            )
        },
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            ScreenHeader(
                title = if (viewModel.isEdit) "Edit Brew" else "New Brew",
                onBack = onDone,
                subtitle = subtitle,
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            ) {
                StepperField("DOSE", form.dose, viewModel::onDoseChange, { viewModel.step(StepField.DOSE, it) }, unit = "g", required = true)
                FormDivider()
                StepperField("GRIND", form.grind, viewModel::onGrindChange, { viewModel.step(StepField.GRIND, it) })
                FormDivider()
                StepperField("TIME", form.time, viewModel::onTimeChange, { viewModel.step(StepField.TIME, it) }, unit = "s", keyboardType = KeyboardType.Number)
                FormDivider()
                StepperField("YIELD", form.yieldG, viewModel::onYieldChange, { viewModel.step(StepField.YIELD, it) }, unit = "g", required = true)
                FormDivider()
                LabeledRow("RATIO") { RatioPill(form.ratio) }
                FormDivider()
                StepperField("TEMP", form.temp, viewModel::onTempChange, { viewModel.step(StepField.TEMP, it) }, unit = "°")
                FormDivider()
                StepperField("VOL", form.vol, viewModel::onVolChange, { viewModel.step(StepField.VOL, it) }, unit = "ml", keyboardType = KeyboardType.Number)
                FormDivider()
                SwitchRow("DISP", form.usedDispenser, viewModel::onDispenserChange)
                FormDivider()
                SwitchRow("LEVEL", form.usedLeveler, viewModel::onLevelerChange)
                FormDivider()
                SwitchRow("FAV", form.isFavorite, viewModel::onFavoriteChange)
                FormDivider()
                LabeledRow("SCORE") {
                    RatingStars(rating = form.rating, editable = true, onRatingChange = viewModel::onRatingChange)
                }
                FormDivider()
                Column(Modifier.padding(top = 12.dp, bottom = 16.dp)) {
                    FieldLabel("NOTES")
                    InsetTextField(
                        value = form.notes,
                        onValueChange = viewModel::onNotesChange,
                        placeholder = "Tasting notes…",
                        singleLine = false,
                        mono = true,
                        modifier = Modifier.padding(top = 7.dp).heightIn(min = 96.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun FormDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
}

@Composable
private fun LabeledRow(label: String, content: @Composable () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FieldLabel(label)
        content()
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    LabeledRow(label) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                checkedThumbColor = MaterialTheme.colorScheme.onTertiary,
                checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                uncheckedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            ),
        )
    }
}
