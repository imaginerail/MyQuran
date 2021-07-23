package com.aneeq.myquran.models

data class OriginalJuz(
    val surahNum:Int,
    val ayahNum: Int,
    val text: String,
    var key: Int,
    var type: Int
)
