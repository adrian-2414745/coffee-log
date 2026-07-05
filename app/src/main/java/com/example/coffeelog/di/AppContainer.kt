package com.example.coffeelog.di

import android.content.Context
import com.example.coffeelog.data.CoffeeRepository
import com.example.coffeelog.data.ThemeRepository
import com.example.coffeelog.data.db.CoffeeLogDatabase
import com.example.coffeelog.data.transfer.DataTransferManager

/**
 * Manual dependency container owned by [com.example.coffeelog.CoffeeLogApp].
 * Builds the database lazily and exposes the repositories.
 */
class AppContainer(private val appContext: Context) {
    private val database: CoffeeLogDatabase by lazy { CoffeeLogDatabase.build(appContext) }
    val coffeeRepository: CoffeeRepository by lazy { CoffeeRepository(database) }
    val themeRepository: ThemeRepository by lazy { ThemeRepository(appContext) }
    val dataTransferManager: DataTransferManager by lazy { DataTransferManager(coffeeRepository) }
}
