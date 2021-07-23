package com.aneeq.myquran.database.favouritesdatabase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FavouritesEntity::class], version = 3)
abstract class FavouritesDatabase: RoomDatabase() {
    abstract fun restaurantDao(): FavouritesDao
}