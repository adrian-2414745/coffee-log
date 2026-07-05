package io.github.adrian2414745.coffeelog.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BrewDao {

    @Insert
    suspend fun insert(brew: BrewEntity): Long

    @Update
    suspend fun update(brew: BrewEntity)

    @Delete
    suspend fun delete(brew: BrewEntity)

    @Query("SELECT * FROM brews WHERE id = :brewId")
    suspend fun getById(brewId: Long): BrewEntity?

    @Query("SELECT * FROM brews WHERE coffeeId = :coffeeId ORDER BY createdAt DESC")
    fun observeForCoffee(coffeeId: Long): Flow<List<BrewEntity>>
}
