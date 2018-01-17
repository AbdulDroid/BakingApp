package com.example.floating.bakingapp.database

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

import com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.Companion.CONTENT_URI
import com.example.floating.bakingapp.database.RecipeContract.RecipeEntry.Companion.TABLE_NAME

@SuppressLint("Registered")
/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
class Provider : ContentProvider() {

    private var mRecipeDbHelper: RecipeDbHelper? = null

    override fun onCreate(): Boolean {
        val context = context
        mRecipeDbHelper = RecipeDbHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val database = mRecipeDbHelper!!.readableDatabase
        val match = uriMatcher.match(uri)
        val mCursor: Cursor
        when (match) {
            FAVORITE -> mCursor = database.query(TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder)
            else -> throw UnsupportedOperationException("Unknown Uri: " + uri)
        }
        mCursor.setNotificationUri(context!!.contentResolver, uri)
        return mCursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val database = mRecipeDbHelper!!.writableDatabase

        val match = uriMatcher.match(uri)

        val mUri: Uri

        when (match) {
            FAVORITE -> {
                val id = database.insert(TABLE_NAME, null, values)
                Log.i("RecipeProvider", id.toString() + "")

                if (id > 0) {
                    mUri = ContentUris.withAppendedId(CONTENT_URI, id)
                } else {
                    throw android.database.SQLException("Failed to insert row into " + uri)
                }
            }
            else -> throw UnsupportedOperationException("Unknown Uri: " + uri)
        }
        context!!.contentResolver.notifyChange(uri, null)
        return mUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        val database = mRecipeDbHelper!!.writableDatabase

        val match = uriMatcher.match(uri)

        val favorite_deleted: Int
        if (selection == null)
            selection = "1"

        when (match) {
            FAVORITE -> favorite_deleted = database.delete(TABLE_NAME, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown Uri " + uri)
        }

        if (favorite_deleted != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return favorite_deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val database = mRecipeDbHelper!!.writableDatabase

        val match = uriMatcher.match(uri)
        val rows_changed: Int

        when (match) {
            FAVORITE -> rows_changed = database.update(TABLE_NAME, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Insertion not supported for " + uri)
        }

        if (rows_changed != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rows_changed
    }

    companion object {

        val FAVORITE = 100
        val uriMatcher = buildUriMatcher()

        private fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(RecipeContract.CONTENT_AUTHORITY, RecipeContract.PATH_FAVORITE_RECIPE, FAVORITE)

            return uriMatcher
        }
    }
}
