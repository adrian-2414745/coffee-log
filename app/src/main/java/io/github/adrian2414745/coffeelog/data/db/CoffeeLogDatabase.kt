package io.github.adrian2414745.coffeelog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CoffeeEntity::class, BrewEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class CoffeeLogDatabase : RoomDatabase() {
    abstract fun coffeeDao(): CoffeeDao
    abstract fun brewDao(): BrewDao

    companion object {
        fun build(context: Context): CoffeeLogDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                CoffeeLogDatabase::class.java,
                "coffeelog.db",
            ).build()
    }
}
