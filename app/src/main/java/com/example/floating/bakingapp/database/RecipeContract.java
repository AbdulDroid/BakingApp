package com.example.floating.bakingapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.floating.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_RECIPE = "favorite_recipe";

    public static class RecipeEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_RECIPE)
                .build();

        public static final String TABLE_NAME = "recipes";
        public static final String _ID = "id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_INGREDIENT_LIST = "recipe_ingredient_list";
    }
}
