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
}
