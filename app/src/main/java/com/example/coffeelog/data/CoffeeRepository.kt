package com.example.coffeelog.data

import androidx.room.withTransaction
import com.example.coffeelog.data.db.BrewEntity
import com.example.coffeelog.data.db.CoffeeEntity
import com.example.coffeelog.data.db.CoffeeLogDatabase
import com.example.coffeelog.data.db.DashboardRow
import kotlinx.coroutines.flow.Flow

/**
 * Single repository over both DAOs. All reads are [Flow]s so screens update live.
 */
class CoffeeRepository(private val db: CoffeeLogDatabase) {
    private val coffeeDao = db.coffeeDao()
    private val brewDao = db.brewDao()

    // Dashboard
    fun observeDashboard(): Flow<List<DashboardRow>> = coffeeDao.observeDashboard()

    // Coffees
    fun observeCoffeeName(coffeeId: Long): Flow<String?> = coffeeDao.observeName(coffeeId)
    suspend fun getCoffee(coffeeId: Long): CoffeeEntity? = coffeeDao.getById(coffeeId)
    suspend fun addCoffee(coffee: CoffeeEntity): Long = coffeeDao.insert(coffee)
    suspend fun updateCoffee(coffee: CoffeeEntity) = coffeeDao.update(coffee)
    suspend fun deleteCoffee(coffeeId: Long) = coffeeDao.deleteById(coffeeId)

    // Brews
    fun observeBrews(coffeeId: Long): Flow<List<BrewEntity>> = brewDao.observeForCoffee(coffeeId)
    suspend fun getBrew(brewId: Long): BrewEntity? = brewDao.getById(brewId)
    suspend fun addBrew(brew: BrewEntity): Long = brewDao.insert(brew)
    suspend fun updateBrew(brew: BrewEntity) = brewDao.update(brew)
    suspend fun deleteBrew(brew: BrewEntity) = brewDao.delete(brew)

    // Import/export helpers
    suspend fun getAllCoffees(): List<CoffeeEntity> = coffeeDao.getAllCoffees()
    suspend fun getAllBrews(): List<BrewEntity> = coffeeDao.getAllBrews()

    /**
     * Replace-all import: wipe every coffee (cascade wipes brews) and insert the
     * provided data in a single transaction. [brewsByCoffeeIndex] maps the index of
     * a coffee in [coffees] to the brews that belong to it.
     */
    suspend fun replaceAll(
        coffees: List<CoffeeEntity>,
        brewsByCoffeeIndex: List<List<BrewEntity>>,
    ) {
        db.withTransaction {
            coffeeDao.deleteAll()
            coffees.forEachIndexed { index, coffee ->
                val newCoffeeId = coffeeDao.insert(coffee.copy(id = 0))
                brewsByCoffeeIndex[index].forEach { brew ->
                    brewDao.insert(brew.copy(id = 0, coffeeId = newCoffeeId))
                }
            }
        }
    }
}
