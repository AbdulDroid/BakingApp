package com.kafilicious.abdul.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.kafilicious.abdul.bakingapp.R;
import com.kafilicious.abdul.bakingapp.adapters.RecipeIngredientsAdapter;
import com.kafilicious.abdul.bakingapp.adapters.RecipeStepsAdapter;
import com.kafilicious.abdul.bakingapp.data.Ingredients;
import com.kafilicious.abdul.bakingapp.data.Steps;

import java.util.ArrayList;

import static com.kafilicious.abdul.bakingapp.adapters.RecipeAdapter.recipe_list;

/**
 * Created by Abdulkarim on 6/14/2017.
 */

public class RecipesListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a
     * tablet device.
     */
    private boolean mTwoPane;
    private ArrayList<Steps> steps;
    private ArrayList<Ingredients> ingredientsList;
    private RecyclerView stepsRecyclerView, ingredientRecyclerView;
    private RecyclerView.LayoutManager layoutManager, layoutManager1;
//    String RECIPE_NAME = "recipe_name";
//    String RECIPE_STEPS_LIST = "recipe_steps";
//    String RECIPE_INGREDIENTS_LIST = "recipe_ingredients";
    String RECIPE_INDEX = "index";
    int index = 0;
    RecipeStepsAdapter adapter;
    RecipeIngredientsAdapter adapter1;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        if (intent != null){
            index = intent.getIntExtra(RECIPE_INDEX, 0);
            steps = recipe_list.get(index).getSteps();
            ingredientsList = recipe_list.get(index).getIngredients();
            toolbar.setTitle(recipe_list.get(index).getName());
        }

        if (findViewById(R.id.step_description_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        stepsRecyclerView = (RecyclerView) findViewById(R.id.recipe_steps_rv);
        stepsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeStepsAdapter(steps, mTwoPane);
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepsRecyclerView.setAdapter(adapter);
        ingredientRecyclerView = (RecyclerView) findViewById(R.id.recipe_ingredients_rv);
        ingredientRecyclerView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingredientRecyclerView.setLayoutManager(layoutManager1);
        adapter1 = new RecipeIngredientsAdapter(ingredientsList);
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientRecyclerView.setAdapter(adapter1);

    }

}
