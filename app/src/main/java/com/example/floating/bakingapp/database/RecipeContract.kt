package com.example.floating.bakingapp.database

import android.net.Uri
import android.provider.BaseColumns

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

object RecipeContract {

    val CONTENT_AUTHORITY = "com.example.floating.bakingapp"
    val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)!!
    val PATH_FAVORITE_RECIPE = "favorite_recipe"

    class RecipeEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_FAVORITE_RECIPE)
                    .build()

            val TABLE_NAME = "recipes"
            val _ID = "id"
            val COLUMN_RECIPE_NAME = "recipe_name"
            val COLUMN_INGREDIENT_LIST = "recipe_ingredient_list"
        }
    }
}
