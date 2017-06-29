package com.example.floating.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Steps;
import com.example.floating.bakingapp.fragments.RecipeDetailsFragment;

import java.util.ArrayList;

import timber.log.Timber;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.example.floating.bakingapp.fragments.RecipeDetailsFragment.ITEM_ID;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity {

    private static int index = 0;
    public static final String STEP_INDEX = "index";
    public static final String RECIPE_STEPS = "recipe_steps";
    public static final String TITLE = "title";
    public static final String STEPS = "steps";
    private ArrayList<Steps> steps;
    private String title;
    private FloatingActionButton fab, fab1;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);//Remove titlebar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_recipe_detail);

        if (orientation == ORIENTATION_PORTRAIT) {
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(STEP_INDEX)) {
                index = savedInstanceState.getInt(STEP_INDEX);
                steps = savedInstanceState.getParcelableArrayList(RECIPE_STEPS);
                title = savedInstanceState.getString(TITLE);
                getSupportActionBar().setTitle(title);
            } else {
                final Intent intent = this.getIntent();

                if (intent != null && intent.hasExtra(ITEM_ID)) {
                    index = intent.getIntExtra(ITEM_ID, 0);
                    steps = intent.getParcelableArrayListExtra(STEPS);
                    title = intent.getStringExtra(TITLE);
                    getSupportActionBar().setTitle(title);
                }
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (index > 0 && index < (steps.size()-1)) {
                        --index;
                        if(index < 0)
                            index = 0;

                        fragmentTransaction(index);
                        Timber.e(RecipeDetailsActivity.class.getSimpleName(), String.valueOf(index));
                    } else {
                        if (index == steps.size()-1) {
                            index = steps.size() - 2;
                            fragmentTransaction(index);
                        } else {
                            index = 0;
                            Toast.makeText(getApplicationContext(), "This is the first step",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (index < (steps.size()-1)) {
                        ++index;
                        if (index > steps.size() - 1)
                            index = steps.size() -1;

                        fragmentTransaction(index);
                    } else {
                        Toast.makeText(getApplicationContext(), "This is the last Step",
                                Toast.LENGTH_LONG).show();
                        index = steps.size() - 1;
                    }


                }
            });

        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ITEM_ID, getIntent().getIntExtra(ITEM_ID, 0));
            arguments.putParcelableArrayList(RECIPE_STEPS, steps);
            RecipeDetailsFragment fragment = new RecipeDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_details_container, fragment)
                    .commit();
        }
    }

    public void fragmentTransaction(int index){
        Bundle arguments = new Bundle();
        arguments.putInt(ITEM_ID, index);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_details_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_INDEX, index);
        outState.putParcelableArrayList(RECIPE_STEPS, steps);
        outState.putString(TITLE, title);
    }
}
