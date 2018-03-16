package com.example.floating.bakingapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.example.floating.bakingapp.R.id.description
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Steps(var id: Int,
                 var shortDescription: String,
                 var description: String,
                 var videoURL: String,
                 var thumbnailURL: String) : Parcelable