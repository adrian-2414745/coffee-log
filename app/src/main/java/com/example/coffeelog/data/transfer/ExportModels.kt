package com.example.coffeelog.data.transfer

import kotlinx.serialization.Serializable

/** JSON schema (versioned). Brews are nested under coffees so the file carries no DB IDs. */
@Serializable
data class ExportRoot(
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val exportedAt: String,
    val coffees: List<ExportCoffee>,
) {
    companion object {
        const val CURRENT_SCHEMA_VERSION = 1
    }
}

@Serializable
data class ExportCoffee(
    val name: String,
    val roastLevel: String? = null,
    val createdAt: Long,
    val brews: List<ExportBrew> = emptyList(),
)

@Serializable
data class ExportBrew(
    val groundsWeightG: Double,
    val liquidWeightG: Double,
    val liquidVolumeMl: Double? = null,
    val grindSize: Double? = null,
    val brewTimeSec: Int? = null,
    val waterTemp: Double? = null,
    val usedDispenser: Boolean = false,
    val usedLeveler: Boolean = false,
    val rating: Int? = null,
    val isFavorite: Boolean = false,
    val favoritedAt: Long? = null,
    val notes: String? = null,
    val createdAt: Long,
)
