package com.example.floating.bakingapp.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@ContentProvider(
        authority = Provider.AUTHORITY,
        database = RecipeDbHelper.class)
public final class Provider {

    public static final String AUTHORITY = "com.example.floating.bakingapp.database.provider";

    @TableEndpoint(table = RecipeDbHelper.RECIPE_INGREDIENTS)
    public static class RecipeIngredients{

        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = RecipeContract.RECIPE_ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/ingredients");

        interface Path{
            String INGREDIENTS = "ingredients";
        }

        @InexactContentUri(
                path = Path.INGREDIENTS + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = RecipeContract.COLUMN_ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return Uri.parse("content://" + AUTHORITY + "/ingredients/" + id);
        }
    }

}
