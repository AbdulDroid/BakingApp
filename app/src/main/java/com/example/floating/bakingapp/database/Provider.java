package com.example.floating.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.floating.bakingapp.data.Recipe;
import com.example.floating.bakingapp.utils.RecipeUtils;

import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.COLUMN_INGREDIENT_LIST;
import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME;
import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.CONTENT_URI;
import static com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.TABLE_NAME;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
public final class Provider extends ContentProvider{

    public static final int FAVORITE = 100;
    public static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY, RecipeContract.PATH_FAVORITE_RECIPE, FAVORITE);

        return uriMatcher;
    }

    private RecipeDbHelper mRecipeDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mRecipeDbHelper = new RecipeDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase database = mRecipeDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor mCursor;
         switch (match){
             case FAVORITE:
                 mCursor = database.query(TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder);
                 break;
             default:
                 throw new UnsupportedOperationException("Unknown Uri: " + uri);
         }
         mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = mRecipeDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        Uri mUri;

        switch (match){
            case FAVORITE:
                long id = database.insert(TABLE_NAME, null, values);
                Log.i("RecipeProvider", id + "");

                if (id > 0){
                    mUri = ContentUris.withAppendedId(CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return mUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mRecipeDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int favorite_deleted;
        if (selection == null)
            selection = "1";

        switch (match){
            case FAVORITE:
                favorite_deleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        if (favorite_deleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favorite_deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mRecipeDbHelper.getWritableDatabase();

        final int match = uriMatcher.match(uri);
        int rows_changed;

        switch (match){
            case FAVORITE:
                rows_changed = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }

        if (rows_changed != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows_changed;
    }

    //table methods for widget purposes

    public long addViewedRecipe(Recipe recipe) {

        SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPE_NAME, recipe.getName());
        values.put(COLUMN_INGREDIENT_LIST, RecipeUtils.getIngredientsString(recipe.getIngredients()));

        return db.insert(TABLE_NAME, null, values);
    }

    public long updateRecipe(Recipe recipe) {

        SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

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
        if (mRecipeDbHelper != null) {
            SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();

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
        return null;
    }

    public int getRecipeDBSize(int id) {

        SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();

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
