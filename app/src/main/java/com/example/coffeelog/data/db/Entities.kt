package com.example.coffeelog.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "coffees")
data class CoffeeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val roastLevel: String?,       // "LIGHT" | "MEDIUM" | "DARK"
    val createdAt: Long,           // epoch millis
)

@Entity(
    tableName = "brews",
    foreignKeys = [
        ForeignKey(
            entity = CoffeeEntity::class,
            parentColumns = ["id"],
            childColumns = ["coffeeId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("coffeeId")],
)
data class BrewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val coffeeId: Long,
    val groundsWeightG: Double,    // mandatory
    val liquidWeightG: Double,     // mandatory
    val liquidVolumeMl: Double?,   // optional
    val grindSize: Double?,        // e.g. 5.1
    val brewTimeSec: Int?,
    val waterTemp: Double?,        // unit-less
    val usedDispenser: Boolean,
    val usedLeveler: Boolean,
    val rating: Int?,              // 1..5, null = unrated
    val isFavorite: Boolean,
    val favoritedAt: Long?,        // set when favorite toggled on
    val notes: String?,
    val createdAt: Long,           // epoch millis; history sort key + card date
)

/**
 * Dashboard projection: a coffee plus a summary of its most-recently-favorited brew
 * (null columns when the coffee has no favorite).
 */
data class DashboardRow(
    val id: Long,
    val name: String,
    val roastLevel: String?,
    val favBrewId: Long?,
    val favGroundsWeightG: Double?,
    val favGrindSize: Double?,
    val favBrewTimeSec: Int?,
    val favLiquidWeightG: Double?,
)
