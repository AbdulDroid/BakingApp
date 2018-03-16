package com.example.floating.bakingapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.example.floating.bakingapp.model.Recipe
import com.example.floating.bakingapp.utils.RecipeUtils

import com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.Companion.COLUMN_INGREDIENT_LIST
import com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.Companion.COLUMN_RECIPE_NAME
import com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.Companion.TABLE_NAME
import com.example.floating.bakingapp.model.Ingredients
import com.example.floating.bakingapp.model.Steps

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
class RecipeDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    val viewedRecipe: Recipe
        get() {
            val db = this.readableDatabase

            val cursor = db.query(
                    TABLE_NAME,
                    null, null, null, null, null, null
            )

            val recipe = Recipe(0, "", null, null, 0, "", "")

            if (cursor.moveToFirst()) {
                do {
                    val nameIndex = cursor.getColumnIndex(COLUMN_RECIPE_NAME)
                    val ingredientIndex = cursor.getColumnIndex(COLUMN_INGREDIENT_LIST)
                    recipe.name = cursor.getString(nameIndex)
                    recipe.ingredient_string = cursor.getString(ingredientIndex)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return recipe
        }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        //Create Table SQLite Query string
        val CREATE_TABLE_RECIPE = ("CREATE TABLE "
                + TABLE_NAME + "("
                + RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RECIPE_NAME + " TEXT NOT NULL,"
                + COLUMN_INGREDIENT_LIST + " TEXT NOT NULL"
                + ");")
        sqLiteDatabase.execSQL(CREATE_TABLE_RECIPE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    //Table methods for widget purposes

    fun addViewedRecipe(recipe: Recipe): Long {

        val db = this.writableDatabase

        val values = ContentValues()

        values.put(COLUMN_RECIPE_NAME, recipe.name)
        values.put(COLUMN_INGREDIENT_LIST, RecipeUtils.getIngredientsString(recipe.ingredients))

        return db.insert(TABLE_NAME, null, values)
    }

    fun updateRecipe(recipe: Recipe): Long {

        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_RECIPE_NAME, recipe.name)
        values.put(COLUMN_INGREDIENT_LIST, RecipeUtils.getIngredientsString(recipe.ingredients))

        val selection = RecipeContract.RecipeEntry._ID + "=?"
        val selectionArgs = arrayOf(0.toString())

        val updateId = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        ).toLong()

        db.close()
        return updateId
    }

    fun getRecipeDBSize(id: Int): Int {
        val db = this.readableDatabase

        val cursor = db.query(
                TABLE_NAME, null, null, null, null,
                null, null)

        val size = cursor.count
        cursor.close()

        return size
    }

    companion object {

        //Database version
        private const val VERSION = 1

        //Database name
        private const val DATABASE_NAME = "recipe.db"
    }
}
