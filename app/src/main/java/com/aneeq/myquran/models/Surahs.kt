package com.aneeq.myquran.models

data class Surahs(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val ayahs: List<OriginalAyahs>
)
