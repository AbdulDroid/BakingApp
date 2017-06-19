package com.kafilicious.abdul.bakingapp.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abdulkarim on 6/12/2017.
 */

public class RecipeContract {

    public static final String AUTHORITY = "com.kafilicious.abdul.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TASKS = "recipes";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "recipes";
        public static final String TABLE_NAME_ONE = "recipe_steps";
        public static final String TABLE_NAME_TWO = "recipe_ingredients";
        public static final String COLUMN_RECIPE_NAME = "name";
        public static final String COLUMN_RECIPE_ID = "id";
        public static final String COLUMN_RECIPE_SERVINGS = "servings";
        public static final String COLUMN_RECIPE_IMAGE = "image";
        public static final String COLUMN_STEP_ID = "id";
        public static final String COLUMN_STEP_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_STEP_DESCRIPTION = "description";
        public static final String COLUMN_STEP_VIDEO_URL = "video_url";
        public static final String COLUMN_STEP_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
        public static final String COLUMN_INGREDIENT_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient";
    }
}
