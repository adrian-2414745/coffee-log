package io.github.adrian2414745.coffeelog.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.adrian2414745.coffeelog.CoffeeLogApp

/** Reach the [AppContainer] from inside a `viewModelFactory { initializer { ... } }`. */
fun CreationExtras.appContainer(): AppContainer =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoffeeLogApp).container
