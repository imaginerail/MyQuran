package com.aneeq.myquran.database.favouritesdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavouritesDao {
    @Insert
    fun insertRes(restaurantEntity: FavouritesEntity)

    @Delete
    fun deleteRes(restaurantEntity: FavouritesEntity)

    @Query("SELECT * FROM favourites")
    fun getAllRestaurants(): List<FavouritesEntity>


    @Query("SELECT * FROM favourites WHERE number=:num ")
    fun getRestaurantById(num: String): FavouritesEntity
}