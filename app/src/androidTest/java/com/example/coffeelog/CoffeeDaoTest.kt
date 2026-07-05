package com.example.coffeelog

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.coffeelog.data.db.BrewDao
import com.example.coffeelog.data.db.BrewEntity
import com.example.coffeelog.data.db.CoffeeDao
import com.example.coffeelog.data.db.CoffeeEntity
import com.example.coffeelog.data.db.CoffeeLogDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoffeeDaoTest {

    private lateinit var db: CoffeeLogDatabase
    private lateinit var coffeeDao: CoffeeDao
    private lateinit var brewDao: BrewDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoffeeLogDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        coffeeDao = db.coffeeDao()
        brewDao = db.brewDao()
    }

    @After
    fun tearDown() = db.close()

    private fun brew(
        coffeeId: Long,
        createdAt: Long,
        isFavorite: Boolean = false,
        favoritedAt: Long? = null,
        grounds: Double = 18.0,
    ) = BrewEntity(
        coffeeId = coffeeId,
        groundsWeightG = grounds,
        liquidWeightG = 36.0,
        liquidVolumeMl = null,
        grindSize = 5.1,
        brewTimeSec = 28,
        waterTemp = 93.0,
        usedDispenser = false,
        usedLeveler = false,
        rating = 4,
        isFavorite = isFavorite,
        favoritedAt = favoritedAt,
        notes = null,
        createdAt = createdAt,
    )

    @Test
    fun dashboard_isSortedAlphabeticallyCaseInsensitive() = runTest {
        coffeeDao.insert(CoffeeEntity(name = "ethiopia", roastLevel = null, createdAt = 1))
        coffeeDao.insert(CoffeeEntity(name = "Brazil", roastLevel = null, createdAt = 2))
        coffeeDao.insert(CoffeeEntity(name = "colombia", roastLevel = null, createdAt = 3))

        val rows = coffeeDao.observeDashboard().first()
        assertEquals(listOf("Brazil", "colombia", "ethiopia"), rows.map { it.name })
    }

    @Test
    fun brews_areNewestFirst() = runTest {
        val id = coffeeDao.insert(CoffeeEntity(name = "Brazil", roastLevel = null, createdAt = 1))
        brewDao.insert(brew(id, createdAt = 100))
        brewDao.insert(brew(id, createdAt = 300))
        brewDao.insert(brew(id, createdAt = 200))

        val brews = brewDao.observeForCoffee(id).first()
        assertEquals(listOf(300L, 200L, 100L), brews.map { it.createdAt })
    }

    @Test
    fun dashboard_joinsMostRecentlyFavoritedBrew() = runTest {
        val id = coffeeDao.insert(CoffeeEntity(name = "Brazil", roastLevel = null, createdAt = 1))
        // Older-created brew favorited most recently should win.
        brewDao.insert(brew(id, createdAt = 500, isFavorite = true, favoritedAt = 1000, grounds = 20.0))
        brewDao.insert(brew(id, createdAt = 900, isFavorite = true, favoritedAt = 2000, grounds = 15.0))
        brewDao.insert(brew(id, createdAt = 950, isFavorite = false, favoritedAt = null, grounds = 99.0))

        val row = coffeeDao.observeDashboard().first().single()
        assertEquals(15.0, row.favGroundsWeightG!!, 0.0001)
    }

    @Test
    fun dashboard_nullFavoriteWhenNoneFavorited() = runTest {
        val id = coffeeDao.insert(CoffeeEntity(name = "Brazil", roastLevel = null, createdAt = 1))
        brewDao.insert(brew(id, createdAt = 500, isFavorite = false))

        val row = coffeeDao.observeDashboard().first().single()
        assertNull(row.favBrewId)
        assertNull(row.favGroundsWeightG)
    }

    @Test
    fun deletingCoffee_cascadesToBrews() = runTest {
        val id = coffeeDao.insert(CoffeeEntity(name = "Brazil", roastLevel = null, createdAt = 1))
        brewDao.insert(brew(id, createdAt = 100))
        brewDao.insert(brew(id, createdAt = 200))

        coffeeDao.deleteById(id)

        assertTrue(brewDao.observeForCoffee(id).first().isEmpty())
        assertTrue(coffeeDao.observeDashboard().first().isEmpty())
    }
}
