package com.example.coffeelog

import com.example.coffeelog.data.transfer.DataTransferManager
import com.example.coffeelog.data.transfer.ExportBrew
import com.example.coffeelog.data.transfer.ExportCoffee
import com.example.coffeelog.data.transfer.ExportRoot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class DataTransferManagerTest {

    private fun sampleRoot() = ExportRoot(
        exportedAt = "2026-07-04T10:00:00Z",
        coffees = listOf(
            ExportCoffee(
                name = "Ethiopia Yirgacheffe",
                roastLevel = "MEDIUM",
                createdAt = 1_719_999_999_000,
                brews = listOf(
                    ExportBrew(
                        groundsWeightG = 18.0,
                        liquidWeightG = 36.5,
                        liquidVolumeMl = null,
                        grindSize = 5.1,
                        brewTimeSec = 28,
                        waterTemp = 93.0,
                        usedDispenser = true,
                        usedLeveler = false,
                        rating = 4,
                        isFavorite = true,
                        favoritedAt = 1_719_999_999_000,
                        notes = "Juicy, bergamot top.",
                        createdAt = 1_719_999_999_000,
                    ),
                ),
            ),
        ),
    )

    @Test
    fun roundTrip_isIdentity() {
        val root = sampleRoot()
        val decoded = DataTransferManager.decode(DataTransferManager.encode(root))
        assertEquals(root, decoded)
    }

    @Test
    fun decode_rejectsMalformedJson() {
        val e = assertThrows(IllegalArgumentException::class.java) { DataTransferManager.decode("{ not json ]") }
        assertTrue(e.message!!.contains("valid"))
    }

    @Test
    fun decode_rejectsWrongSchemaVersion() {
        val json = """{"schemaVersion":2,"exportedAt":"x","coffees":[]}"""
        val e = assertThrows(IllegalArgumentException::class.java) { DataTransferManager.decode(json) }
        assertTrue(e.message!!.contains("version"))
    }

    @Test
    fun decode_rejectsInvalidRating() {
        val bad = sampleRoot().let { root ->
            root.copy(coffees = root.coffees.map { c -> c.copy(brews = c.brews.map { it.copy(rating = 9) }) })
        }
        val e = assertThrows(IllegalArgumentException::class.java) { DataTransferManager.decode(DataTransferManager.encode(bad)) }
        assertTrue(e.message!!.contains("rating"))
    }

    @Test
    fun decode_rejectsNonPositiveDose() {
        val bad = sampleRoot().let { root ->
            root.copy(coffees = root.coffees.map { c -> c.copy(brews = c.brews.map { it.copy(groundsWeightG = 0.0) }) })
        }
        assertThrows(IllegalArgumentException::class.java) { DataTransferManager.decode(DataTransferManager.encode(bad)) }
    }

}
