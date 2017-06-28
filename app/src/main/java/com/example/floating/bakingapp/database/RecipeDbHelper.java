package com.example.floating.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
public  class RecipeDbHelper extends SQLiteOpenHelper {

    //Database version
    private static final int VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "recipe.db";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create Table SQLite Query string
        final String CREATE_TABLE_RECIPE = "CREATE TABLE "
                + RecipeContract.RecipeEntry.TABLE_NAME + "("
                + RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY,"
                + RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL,"
                + RecipeContract.RecipeEntry.COLUMN_INGREDIENT_LIST + " TEXT NOT NULL"
                + ");";
        sqLiteDatabase.execSQL(CREATE_TABLE_RECIPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
