package com.example.floating.bakingapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@SuppressLint("ParcelCreator")
@Parcelize
class Steps : Parcelable {

    @SerializedName("id")
    @Expose
    val id: Int = 0
    @SerializedName("shortDescription")
    @Expose
    val shortDescription: String? = null
    @SerializedName("description")
    @Expose
    val description: String? = null
    @SerializedName("videoURL")
    @Expose
    val videoURL: String? = null
    @SerializedName("thumbnailURL")
    @Expose
    val thumbnailURL: String? = null

    override fun toString(): String {
        return "Steps{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}'
    }
}