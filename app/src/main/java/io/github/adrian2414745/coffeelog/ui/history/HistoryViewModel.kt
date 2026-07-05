package io.github.adrian2414745.coffeelog.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import io.github.adrian2414745.coffeelog.data.CoffeeRepository
import io.github.adrian2414745.coffeelog.data.db.BrewEntity
import io.github.adrian2414745.coffeelog.di.appContainer
import io.github.adrian2414745.coffeelog.ui.navigation.History
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: CoffeeRepository,
    private val coffeeId: Long,
) : ViewModel() {

    val coffeeName: StateFlow<String> = repository.observeCoffeeName(coffeeId)
        .map { it ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val brews: StateFlow<List<BrewEntity>> = repository.observeBrews(coffeeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteBrew(brew: BrewEntity) {
        viewModelScope.launch { repository.deleteBrew(brew) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val coffeeId = createSavedStateHandle().toRoute<History>().coffeeId
                HistoryViewModel(appContainer().coffeeRepository, coffeeId)
            }
        }
    }
}
