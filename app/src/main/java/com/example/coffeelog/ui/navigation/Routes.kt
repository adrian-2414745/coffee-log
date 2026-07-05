package com.example.coffeelog.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Dashboard

@Serializable
data class History(val coffeeId: Long)

@Serializable
data class BrewEdit(val coffeeId: Long, val brewId: Long? = null)

@Serializable
object AddCoffee

@Serializable
object Settings
