package com.example.floating.bakingapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.adapters.RecipeStepsAdapter;
import com.example.floating.bakingapp.data.Ingredients;
import com.example.floating.bakingapp.data.Recipe;
import com.example.floating.bakingapp.data.Steps;
import com.example.floating.bakingapp.database.RecipeContract;
import com.example.floating.bakingapp.database.RecipeDbHelper;
import com.example.floating.bakingapp.fragments.RecipeDetailsFragment;
import com.example.floating.bakingapp.utils.RecipeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a
     * tablet device.
     */
    public static boolean mTwoPane;
    public static ArrayList<Steps> steps;
    public static ArrayList<Ingredients> ingredientsList;
    private LinearLayoutManager layoutManager;
    String STEP_ITEM = "step_item";
    public static final String STEPS_LIST_KEY = "recipe_steps";
    public static final String RECIPE_LIST = "recipe_list";
    private Parcelable stepsListState = null;
    int mPosition = -1, topView, position;
    public static Integer index;
    private Recipe recipe;
    RecipeStepsAdapter adapter;
    private RecipeDbHelper provider;
    String title;

    @BindView(R.id.recipe_ingredients_tv)
    TextView ingredientTextView;
    @BindView(R.id.recipe_steps_rv)
    RecyclerView stepsRecyclerView;
    @BindView(R.id.toolbar_list)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        provider = new RecipeDbHelper(this);




        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RECIPE_LIST)) {

            recipe = intent.getParcelableExtra(RECIPE_LIST);
            steps = recipe.getSteps();
            ingredientsList = recipe.getIngredients();
            title = recipe.getName();

            if (title != null)
                actionBar.setTitle(title);

            ContentValues values = new ContentValues();

            values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,recipe.getName());
            values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_LIST, recipe.getIngredient_string());

            if (isLastViewed() == 1) {
                provider.updateRecipe(recipe);
            }else {
                provider.addViewedRecipe(recipe);
            }

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(RecipeDetailsFragment.ITEM_ID, 0);
                arguments.putBoolean(RecipeDetailsFragment.PANES, mTwoPane);
                RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_step_details_container, fragment)
                        .commit();
            }
        }

        if (findViewById(R.id.recipe_step_details_container) != null) {
            Timber.e("ListActivity", "containerFound");
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            if (savedInstanceState == null) {
                Bundle arguments = new Bundle();
                arguments.putInt(RecipeDetailsFragment.ITEM_ID, 0);
                arguments.putBoolean(RecipeDetailsFragment.PANES, mTwoPane);
                RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_step_details_container, fragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
            Timber.e("ListActivity", "containerNotFound");
        }



        stepsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeStepsAdapter(steps, mTwoPane);
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ingredientTextView.setText(RecipeUtils.getIngredientsString(ingredientsList));

    }

    @Override
    protected void onPause() {
        super.onPause();

        stepsListState = layoutManager.onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepsListState != null)
            layoutManager.onRestoreInstanceState(stepsListState);
        if (mPosition != -1) {
            layoutManager.scrollToPositionWithOffset(mPosition, topView);
            layoutManager.scrollToPosition(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_LIST, recipe);
        stepsListState = layoutManager.onSaveInstanceState();

        outState.putParcelable(STEPS_LIST_KEY, stepsListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        recipe = state.getParcelable(RECIPE_LIST);
        //steps = recipe.getSteps();
        ingredientsList = recipe.getIngredients();
        stepsListState = state.getParcelable(STEPS_LIST_KEY);
    }

    private int isLastViewed() {
        int size = provider.getRecipeDBSize(0);
        if (size == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
}
