package com.example.coffeelog.ui.editcoffee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.coffeelog.data.CoffeeRepository
import com.example.coffeelog.data.db.CoffeeEntity
import com.example.coffeelog.di.appContainer
import com.example.coffeelog.ui.navigation.EditCoffee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditCoffeeUiState(
    val name: String = "",
    val roastLevel: String = "MEDIUM",
) {
    val canSave: Boolean get() = name.isNotBlank()
}

class EditCoffeeViewModel(
    private val repository: CoffeeRepository,
    private val coffeeId: Long,
) : ViewModel() {

    private var original: CoffeeEntity? = null

    private val _state = MutableStateFlow(EditCoffeeUiState())
    val state: StateFlow<EditCoffeeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCoffee(coffeeId)?.let { c ->
                original = c
                _state.value = EditCoffeeUiState(
                    name = c.name,
                    roastLevel = c.roastLevel ?: "MEDIUM",
                )
            }
        }
    }

    fun onNameChange(name: String) = _state.update { it.copy(name = name) }
    fun onRoastChange(roast: String) = _state.update { it.copy(roastLevel = roast) }

    fun save(onSaved: () -> Unit) {
        val current = _state.value
        val orig = original ?: return
        if (!current.canSave) return
        viewModelScope.launch {
            repository.updateCoffee(
                orig.copy(
                    name = current.name.trim(),
                    roastLevel = current.roastLevel,
                ),
            )
            onSaved()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val route = createSavedStateHandle().toRoute<EditCoffee>()
                EditCoffeeViewModel(appContainer().coffeeRepository, route.coffeeId)
            }
        }
    }
}
