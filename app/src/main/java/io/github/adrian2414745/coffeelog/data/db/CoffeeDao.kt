package io.github.adrian2414745.coffeelog.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeDao {

    @Insert
    suspend fun insert(coffee: CoffeeEntity): Long

    @Update
    suspend fun update(coffee: CoffeeEntity)

    @Query("DELETE FROM coffees WHERE id = :coffeeId")
    suspend fun deleteById(coffeeId: Long)

    @Query("DELETE FROM coffees")
    suspend fun deleteAll()

    @Query("SELECT * FROM coffees ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllCoffees(): List<CoffeeEntity>

    @Query("SELECT * FROM brews ORDER BY createdAt ASC")
    suspend fun getAllBrews(): List<BrewEntity>

    @Query("SELECT * FROM coffees WHERE id = :coffeeId")
    suspend fun getById(coffeeId: Long): CoffeeEntity?

    @Query("SELECT name FROM coffees WHERE id = :coffeeId")
    fun observeName(coffeeId: Long): Flow<String?>

    /**
     * All coffees sorted alphabetically (case-insensitive), each joined with its
     * most-recently-favorited brew's headline metrics (null when none).
     */
    @Query(
        """
        SELECT c.id AS id,
               c.name AS name,
               c.roastLevel AS roastLevel,
               b.id AS favBrewId,
               b.groundsWeightG AS favGroundsWeightG,
               b.grindSize AS favGrindSize,
               b.brewTimeSec AS favBrewTimeSec,
               b.liquidWeightG AS favLiquidWeightG
        FROM coffees c
        LEFT JOIN brews b ON b.id = (
            SELECT b2.id FROM brews b2
            WHERE b2.coffeeId = c.id AND b2.isFavorite = 1 AND b2.favoritedAt IS NOT NULL
            ORDER BY b2.favoritedAt DESC
            LIMIT 1
        )
        ORDER BY c.name COLLATE NOCASE ASC
        """,
    )
    fun observeDashboard(): Flow<List<DashboardRow>>
}
