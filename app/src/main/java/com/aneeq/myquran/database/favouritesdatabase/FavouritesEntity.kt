package com.aneeq.myquran.database.favouritesdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourites")
class FavouritesEntity (
    @PrimaryKey val number: String,
    @ColumnInfo(name = "englishNamePara") val englishNamePara: String,
    @ColumnInfo(name = "numberOfAyahsPage") val numberOfAyahsPage: Int,
    @ColumnInfo(name = "revelationType") val revelationType: String,
    @ColumnInfo(name = "page") val page: Int
)