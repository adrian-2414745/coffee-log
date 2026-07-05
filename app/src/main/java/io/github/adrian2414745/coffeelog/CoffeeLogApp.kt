package io.github.adrian2414745.coffeelog

import android.app.Application
import io.github.adrian2414745.coffeelog.di.AppContainer

class CoffeeLogApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
