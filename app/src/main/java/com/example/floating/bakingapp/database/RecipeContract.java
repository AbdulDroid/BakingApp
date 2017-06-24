package com.example.floating.bakingapp.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String RECIPE_ID = "recipe_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String RECIPE_NAME = "recipe_name";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String INGREDIENT_NAME = "ingredient";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String INGREDIENT_MEASURE = "ingredient_measure";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String INGREDEINT_QUANTITY = "ingredient_quantity";
}
