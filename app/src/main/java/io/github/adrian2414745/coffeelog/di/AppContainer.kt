package io.github.adrian2414745.coffeelog.di

import android.content.Context
import io.github.adrian2414745.coffeelog.data.CoffeeRepository
import io.github.adrian2414745.coffeelog.data.ThemeRepository
import io.github.adrian2414745.coffeelog.data.db.CoffeeLogDatabase
import io.github.adrian2414745.coffeelog.data.transfer.DataTransferManager

/**
 * Manual dependency container owned by [io.github.adrian2414745.coffeelog.CoffeeLogApp].
 * Builds the database lazily and exposes the repositories.
 */
class AppContainer(private val appContext: Context) {
    private val database: CoffeeLogDatabase by lazy { CoffeeLogDatabase.build(appContext) }
    val coffeeRepository: CoffeeRepository by lazy { CoffeeRepository(database) }
    val themeRepository: ThemeRepository by lazy { ThemeRepository(appContext) }
    val dataTransferManager: DataTransferManager by lazy { DataTransferManager(coffeeRepository) }
}
