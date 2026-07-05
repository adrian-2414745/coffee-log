package com.example.coffeelog.data.transfer

import com.example.coffeelog.data.CoffeeRepository
import com.example.coffeelog.data.db.BrewEntity
import com.example.coffeelog.data.db.CoffeeEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

/**
 * Export/import of the whole database as versioned JSON.
 * Pure [encode]/[decode] are separated from DB access so they are unit-testable.
 */
class DataTransferManager(private val repository: CoffeeRepository) {

    // ---- pure (delegate to the testable companion functions) ----

    fun encode(root: ExportRoot): String = Companion.encode(root)

    fun decode(text: String): ExportRoot = Companion.decode(text)

    // ---- DB-backed ----

    suspend fun buildExport(): ExportRoot {
        val coffees = repository.getAllCoffees()
        val brewsByCoffee = repository.getAllBrews().groupBy { it.coffeeId }
        return ExportRoot(
            exportedAt = Instant.now().toString(),
            coffees = coffees.map { c ->
                ExportCoffee(
                    name = c.name,
                    roastLevel = c.roastLevel,
                    createdAt = c.createdAt,
                    brews = (brewsByCoffee[c.id] ?: emptyList()).map { it.toExport() },
                )
            },
        )
    }

    suspend fun applyImport(root: ExportRoot) {
        val coffees = root.coffees.map { c ->
            CoffeeEntity(name = c.name, roastLevel = c.roastLevel, createdAt = c.createdAt)
        }
        val brews = root.coffees.map { c ->
            c.brews.map { b -> b.toEntity() }
        }
        repository.replaceAll(coffees, brews)
    }

    private fun BrewEntity.toExport() = ExportBrew(
        groundsWeightG = groundsWeightG,
        liquidWeightG = liquidWeightG,
        liquidVolumeMl = liquidVolumeMl,
        grindSize = grindSize,
        brewTimeSec = brewTimeSec,
        waterTemp = waterTemp,
        usedDispenser = usedDispenser,
        usedLeveler = usedLeveler,
        rating = rating,
        isFavorite = isFavorite,
        favoritedAt = favoritedAt,
        notes = notes,
        createdAt = createdAt,
    )

    private fun ExportBrew.toEntity() = BrewEntity(
        coffeeId = 0, // reassigned in replaceAll
        groundsWeightG = groundsWeightG,
        liquidWeightG = liquidWeightG,
        liquidVolumeMl = liquidVolumeMl,
        grindSize = grindSize,
        brewTimeSec = brewTimeSec,
        waterTemp = waterTemp,
        usedDispenser = usedDispenser,
        usedLeveler = usedLeveler,
        rating = rating,
        isFavorite = isFavorite,
        favoritedAt = favoritedAt,
        notes = notes,
        createdAt = createdAt,
    )

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            encodeDefaults = true
        }

        fun encode(root: ExportRoot): String = json.encodeToString(root)

        /** Parse and validate; throws [IllegalArgumentException] with a user-facing reason on any problem. */
        fun decode(text: String): ExportRoot {
            val root = try {
                json.decodeFromString<ExportRoot>(text)
            } catch (e: Exception) {
                throw IllegalArgumentException("Not a valid Coffee Log backup file.")
            }
            require(root.schemaVersion == ExportRoot.CURRENT_SCHEMA_VERSION) {
                "Unsupported backup version: ${root.schemaVersion}."
            }
            root.coffees.forEach { coffee ->
                require(coffee.name.isNotBlank()) { "A coffee is missing its name." }
                coffee.brews.forEach { brew ->
                    require(brew.groundsWeightG > 0.0) { "A brew has an invalid dose weight." }
                    require(brew.liquidWeightG > 0.0) { "A brew has an invalid yield weight." }
                    require(brew.rating == null || brew.rating in 1..5) { "A brew has an invalid rating." }
                }
            }
            return root
        }
    }
}
