package com.example.floating.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.adapters.RecipeIngredientsAdapter;
import com.example.floating.bakingapp.adapters.RecipeStepsAdapter;
import com.example.floating.bakingapp.data.Ingredients;
import com.example.floating.bakingapp.data.Steps;
import com.example.floating.bakingapp.fragments.RecipeDetailsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.floating.bakingapp.adapters.RecipeAdapter.recipe_list;
import static com.example.floating.bakingapp.fragments.RecipeDetailsFragment.mIndex;

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
    private RecyclerView.LayoutManager layoutManager, layoutManager1;
    //    String RECIPE_NAME = "recipe_name";
//    String RECIPE_STEPS_LIST = "recipe_steps";
//    String RECIPE_INGREDIENTS_LIST = "recipe_ingredients";
    public static final String RECIPE_INDEX = "index";
    public static int index = 0;
    RecipeStepsAdapter adapter;
    RecipeIngredientsAdapter adapter1;
    String title;

    @BindView(R.id.recipe_ingredients_rv)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.recipe_steps_rv)
    RecyclerView stepsRecyclerView;
    @BindView(R.id.toolbar_list)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (savedInstanceState != null){
            index = savedInstanceState.getInt(RECIPE_INDEX);
            steps = recipe_list.get(index).getSteps();
            ingredientsList = recipe_list.get(index).getIngredients();
            actionBar.setTitle(recipe_list.get(index).getName());
        }

        if (intent != null){
            index = intent.getIntExtra(RECIPE_INDEX, 0);
            steps = recipe_list.get(index).getSteps();
            ingredientsList = recipe_list.get(index).getIngredients();
            actionBar.setTitle(recipe_list.get(index).getName());
            if (mTwoPane){
                Bundle arguments = new Bundle();
                arguments.putInt(RecipeDetailsFragment.ITEM_ID, mIndex);
                arguments.putBoolean(RecipeDetailsFragment.PANES, mTwoPane);
                RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_step_details_container, fragment)
                        .commit();
            }
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            Log.e("ListActivity", "containerFound");
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }else {
            Log.e("ListActivity", "containerNotFound");
        }

        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putInt(RecipeDetailsFragment.ITEM_ID, 0);
            arguments.putBoolean(RecipeDetailsFragment.PANES, mTwoPane);
            RecipeDetailsFragment fragment = new RecipeDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_details_container, fragment)
                    .commit();
        }

        stepsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeStepsAdapter(steps, mTwoPane);
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepsRecyclerView.setAdapter(adapter);

        ingredientRecyclerView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingredientRecyclerView.setLayoutManager(layoutManager1);
        adapter1 = new RecipeIngredientsAdapter(ingredientsList);
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientRecyclerView.setAdapter(adapter1);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_INDEX, index);
    }
}
