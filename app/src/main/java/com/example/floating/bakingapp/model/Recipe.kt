package com.example.floating.bakingapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Recipe(var id: Long,
                  var name: String,
                  var ingredients: List<Ingredients>?,
                  var steps: List<Steps>?,
                  var servings: Int,
                  var image: String,
                  var ingredient_string: String?): Parcelable