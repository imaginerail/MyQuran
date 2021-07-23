package com.aneeq.myquran.database.seleddatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SelEntity::class], version = 1)
abstract class SelDatabase: RoomDatabase() {
    abstract fun restaurantDao(): SelDao
}