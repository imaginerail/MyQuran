package com.aneeq.myquran.models

data class JuzList(
    val number: String,
    val name: String,
    var page: Int
) {
    fun getJuzName(): String {
        return name
    }

    fun getJuzNum(): String {
        return number
    }
}


