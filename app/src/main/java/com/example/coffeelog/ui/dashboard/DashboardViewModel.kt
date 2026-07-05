package com.example.coffeelog.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.coffeelog.data.CoffeeRepository
import com.example.coffeelog.data.db.DashboardRow
import com.example.coffeelog.di.appContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: CoffeeRepository) : ViewModel() {

    val rows: StateFlow<List<DashboardRow>> = repository.observeDashboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteCoffee(coffeeId: Long) {
        viewModelScope.launch { repository.deleteCoffee(coffeeId) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { DashboardViewModel(appContainer().coffeeRepository) }
        }
    }
}
