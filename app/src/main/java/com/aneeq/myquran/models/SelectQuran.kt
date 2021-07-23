package com.aneeq.myquran.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectQuran(
    val identifier: String,
    val language: String,
    val name: String,
    val englishName: String,
    val format: String,
    val type: String,
    val direction: String
) : Parcelable