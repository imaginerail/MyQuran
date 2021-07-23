package com.aneeq.myquran.database.seleddatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SelDao {
    @Insert
    fun insertRes(seledEntity: SelEntity)

    @Delete
    fun deleteRes(seledEntity: SelEntity)

    @Query("SELECT * FROM selecteded")
    fun getAllSelEdns(): List<SelEntity>


    @Query("SELECT * FROM selecteded WHERE id=:id ")
    fun getSelEdnById(id: String): SelEntity

    @Query("DELETE FROM selecteded")
    fun clearAll():Int
}