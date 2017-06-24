package com.example.floating.bakingapp.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@Database(version = RecipeDbHelper.VERSION)
public class RecipeDbHelper {

    public static final int VERSION = 1;

    @Table(RecipeContract.class)
    public static final String RECIPE_INGREDIENTS = "recipe_ingredients";
}
