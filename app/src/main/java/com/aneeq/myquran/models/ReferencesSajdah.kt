package com.aneeq.myquran.models

data class ReferencesSajdah(
    val surah: Int,
    val ayah: Int,
    val recommended: Boolean,
    val obligatory: Boolean
)