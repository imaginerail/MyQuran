package com.aneeq.myquran.models

data class SurahList(
    val number: Int,
    val englishName: String,
    val numberOfAyahs: Int,
    val revelationType: String,
    val page: Int
)
