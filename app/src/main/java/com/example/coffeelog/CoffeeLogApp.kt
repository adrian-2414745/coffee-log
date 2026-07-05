package com.example.coffeelog

import android.app.Application
import com.example.coffeelog.di.AppContainer

class CoffeeLogApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
