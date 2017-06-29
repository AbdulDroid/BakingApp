package com.example.floating.bakingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.floating.bakingapp.data.Recipe;
import com.example.floating.bakingapp.utils.RecipeUtils;

import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.COLUMN_INGREDIENT_LIST;
import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME;
import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.TABLE_NAME;

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
                + TABLE_NAME + "("
                + RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RECIPE_NAME + " TEXT NOT NULL,"
                + COLUMN_INGREDIENT_LIST + " TEXT NOT NULL"
                + ");";
        sqLiteDatabase.execSQL(CREATE_TABLE_RECIPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //Table methods for widget purposes

    public long addViewedRecipe(Recipe recipe) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPE_NAME, recipe.getName());
        values.put(COLUMN_INGREDIENT_LIST, RecipeUtils.getIngredientsString(recipe.getIngredients()));

        return db.insert(TABLE_NAME, null, values);
    }

    public long updateRecipe(Recipe recipe) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, recipe.getName());
        values.put(COLUMN_INGREDIENT_LIST, RecipeUtils.getIngredientsString(recipe.getIngredients()));

        String selection = RecipeContract.RecipeEntry._ID + "=?";
        String[] selectrionArgs = {String.valueOf(0)};

        long updateId = db.update(
                TABLE_NAME,
                values,
                selection,
                selectrionArgs
        );

        db.close();
        return updateId;
    }

    public Recipe getViewedRecipe() {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(
                    TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            Recipe recipe = new Recipe();

            if (cursor.moveToFirst()) {
                do {
                    int name_index = cursor.getColumnIndex(COLUMN_RECIPE_NAME);
                    int ingredient_index = cursor.getColumnIndex(COLUMN_INGREDIENT_LIST);
                    recipe.setName(cursor.getString(name_index));
                    recipe.setIngredient_string(cursor.getString(ingredient_index));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return recipe;
        }

    public int getRecipeDBSize(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(
                    TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            int size = cursor.getCount();
            cursor.close();

            return size;
        }
}
