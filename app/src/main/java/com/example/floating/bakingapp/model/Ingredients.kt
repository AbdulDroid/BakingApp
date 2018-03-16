package com.example.floating.bakingapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Ingredients(var quantity: Double,
                       var measure: String,
                       var ingredient: String): Parcelable
