package com.example.floating.bakingapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
class Ingredients : Parcelable {
    val quantity: Double = 0.toDouble()
    val measure: String? = null
    val ingredient: String? = null

    override fun toString(): String {
        return "Ingredients{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}'
    }
}
