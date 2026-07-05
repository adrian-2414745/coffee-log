package io.github.adrian2414745.coffeelog.ui.addcoffee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.adrian2414745.coffeelog.data.CoffeeRepository
import io.github.adrian2414745.coffeelog.data.db.CoffeeEntity
import io.github.adrian2414745.coffeelog.di.appContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddCoffeeUiState(
    val name: String = "",
    val roastLevel: String = "MEDIUM",
) {
    val canSave: Boolean get() = name.isNotBlank()
}

class AddCoffeeViewModel(private val repository: CoffeeRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddCoffeeUiState())
    val state: StateFlow<AddCoffeeUiState> = _state.asStateFlow()

    fun onNameChange(name: String) = _state.update { it.copy(name = name) }
    fun onRoastChange(roast: String) = _state.update { it.copy(roastLevel = roast) }

    fun save(onSaved: () -> Unit) {
        val current = _state.value
        if (!current.canSave) return
        viewModelScope.launch {
            repository.addCoffee(
                CoffeeEntity(
                    name = current.name.trim(),
                    roastLevel = current.roastLevel,
                    createdAt = System.currentTimeMillis(),
                ),
            )
            onSaved()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { AddCoffeeViewModel(appContainer().coffeeRepository) }
        }
    }
}
