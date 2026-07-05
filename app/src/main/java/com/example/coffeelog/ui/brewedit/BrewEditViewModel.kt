package com.example.coffeelog.ui.brewedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.coffeelog.data.CoffeeRepository
import com.example.coffeelog.data.db.BrewEntity
import com.example.coffeelog.di.appContainer
import com.example.coffeelog.ui.navigation.BrewEdit
import com.example.coffeelog.util.RatioFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

enum class StepField(val step: Double, val decimals: Int) {
    DOSE(0.5, 1),
    GRIND(0.1, 1),
    TIME(1.0, 0),
    YIELD(0.5, 1),
    TEMP(1.0, 0),
    VOL(1.0, 0),
}

data class BrewFormState(
    val dose: String = "",
    val grind: String = "",
    val time: String = "",
    val yieldG: String = "",
    val temp: String = "",
    val vol: String = "",
    val usedDispenser: Boolean = false,
    val usedLeveler: Boolean = false,
    val isFavorite: Boolean = false,
    val rating: Int? = null,
    val notes: String = "",
) {
    val ratio: String get() = RatioFormatter.format(dose.toDoubleOrNull(), yieldG.toDoubleOrNull())
    val canSave: Boolean
        get() = (dose.toDoubleOrNull()?.let { it > 0.0 } == true) &&
            (yieldG.toDoubleOrNull()?.let { it > 0.0 } == true)
}

class BrewEditViewModel(
    private val repository: CoffeeRepository,
    private val coffeeId: Long,
    private val brewId: Long?,
) : ViewModel() {

    val isEdit: Boolean = brewId != null
    private var originalBrew: BrewEntity? = null

    val coffeeName: StateFlow<String> = repository.observeCoffeeName(coffeeId)
        .map { it ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    private val _form = MutableStateFlow(BrewFormState())
    val form: StateFlow<BrewFormState> = _form.asStateFlow()

    init {
        if (brewId != null) {
            viewModelScope.launch {
                repository.getBrew(brewId)?.let { b ->
                    originalBrew = b
                    _form.value = BrewFormState(
                        dose = fixed(b.groundsWeightG, 1),
                        grind = optFixed(b.grindSize, 1),
                        time = b.brewTimeSec?.toString() ?: "",
                        yieldG = fixed(b.liquidWeightG, 1),
                        temp = plain(b.waterTemp),
                        vol = plain(b.liquidVolumeMl),
                        usedDispenser = b.usedDispenser,
                        usedLeveler = b.usedLeveler,
                        isFavorite = b.isFavorite,
                        rating = b.rating,
                        notes = b.notes ?: "",
                    )
                }
            }
        }
    }

    fun onDoseChange(v: String) = _form.update { it.copy(dose = sanitize(v, true)) }
    fun onGrindChange(v: String) = _form.update { it.copy(grind = sanitize(v, true)) }
    fun onTimeChange(v: String) = _form.update { it.copy(time = sanitize(v, false)) }
    fun onYieldChange(v: String) = _form.update { it.copy(yieldG = sanitize(v, true)) }
    fun onTempChange(v: String) = _form.update { it.copy(temp = sanitize(v, true)) }
    fun onVolChange(v: String) = _form.update { it.copy(vol = sanitize(v, false)) }

    fun onDispenserChange(v: Boolean) = _form.update { it.copy(usedDispenser = v) }
    fun onLevelerChange(v: Boolean) = _form.update { it.copy(usedLeveler = v) }
    fun onFavoriteChange(v: Boolean) = _form.update { it.copy(isFavorite = v) }
    fun onRatingChange(v: Int?) = _form.update { it.copy(rating = v) }
    fun onNotesChange(v: String) = _form.update { it.copy(notes = v) }

    fun step(field: StepField, delta: Int) {
        _form.update { state ->
            val current = when (field) {
                StepField.DOSE -> state.dose
                StepField.GRIND -> state.grind
                StepField.TIME -> state.time
                StepField.YIELD -> state.yieldG
                StepField.TEMP -> state.temp
                StepField.VOL -> state.vol
            }
            val base = current.toDoubleOrNull() ?: 0.0
            val next = (base + delta * field.step).coerceAtLeast(0.0)
            val formatted = String.format(Locale.US, "%.${field.decimals}f", next)
            when (field) {
                StepField.DOSE -> state.copy(dose = formatted)
                StepField.GRIND -> state.copy(grind = formatted)
                StepField.TIME -> state.copy(time = formatted)
                StepField.YIELD -> state.copy(yieldG = formatted)
                StepField.TEMP -> state.copy(temp = formatted)
                StepField.VOL -> state.copy(vol = formatted)
            }
        }
    }

    fun save(onSaved: () -> Unit) {
        val f = _form.value
        if (!f.canSave) return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val original = originalBrew
            val favoritedAt = when {
                isEdit && original != null && original.isFavorite == f.isFavorite -> original.favoritedAt
                f.isFavorite -> now
                else -> null
            }
            val entity = BrewEntity(
                id = brewId ?: 0,
                coffeeId = coffeeId,
                groundsWeightG = f.dose.toDouble(),
                liquidWeightG = f.yieldG.toDouble(),
                liquidVolumeMl = f.vol.toDoubleOrNull(),
                grindSize = f.grind.toDoubleOrNull(),
                brewTimeSec = f.time.toDoubleOrNull()?.toInt(),
                waterTemp = f.temp.toDoubleOrNull(),
                usedDispenser = f.usedDispenser,
                usedLeveler = f.usedLeveler,
                rating = f.rating,
                isFavorite = f.isFavorite,
                favoritedAt = favoritedAt,
                notes = f.notes.trim().ifBlank { null },
                createdAt = original?.createdAt ?: now,
            )
            if (isEdit) repository.updateBrew(entity) else repository.addBrew(entity)
            onSaved()
        }
    }

    private fun sanitize(input: String, allowDecimal: Boolean): String {
        val filtered = input.filter { it.isDigit() || (allowDecimal && it == '.') }
        if (!allowDecimal) return filtered
        // keep only the first dot
        val firstDot = filtered.indexOf('.')
        if (firstDot < 0) return filtered
        return filtered.substring(0, firstDot + 1) + filtered.substring(firstDot + 1).replace(".", "")
    }

    companion object {
        private fun fixed(v: Double, dec: Int) = String.format(Locale.US, "%.${dec}f", v)
        private fun optFixed(v: Double?, dec: Int) = v?.let { String.format(Locale.US, "%.${dec}f", it) } ?: ""
        private fun plain(v: Double?): String {
            if (v == null) return ""
            return if (v % 1.0 == 0.0) v.toLong().toString() else String.format(Locale.US, "%.1f", v)
        }

        val Factory = viewModelFactory {
            initializer {
                val route = createSavedStateHandle().toRoute<BrewEdit>()
                BrewEditViewModel(appContainer().coffeeRepository, route.coffeeId, route.brewId)
            }
        }
    }
}
