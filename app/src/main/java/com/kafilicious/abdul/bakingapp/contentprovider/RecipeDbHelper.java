package com.kafilicious.abdul.bakingapp.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.kafilicious.abdul.bakingapp.contentprovider.RecipeContract.RecipeEntry.COLUMN_RECIPE_ID;
import static com.kafilicious.abdul.bakingapp.contentprovider.RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE;
import static com.kafilicious.abdul.bakingapp.contentprovider.RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME;
import static com.kafilicious.abdul.bakingapp.contentprovider.RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS;
import static com.kafilicious.abdul.bakingapp.contentprovider.RecipeContract.RecipeEntry.TABLE_NAME;

/**
 * Created by Abdulkarim on 6/12/2017.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final String DATABASE_NAME_ONE = "steps.db";
    private static final String DATABASE_NAME_TWO = "ingredients.db";

    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL, " +
                COLUMN_RECIPE_IMAGE + " TEXT" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
