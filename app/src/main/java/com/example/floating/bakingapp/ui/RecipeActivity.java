package com.example.floating.bakingapp.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.floating.bakingapp.AppController;
import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.adapters.RecipeAdapter;
import com.example.floating.bakingapp.data.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity {

    public static final String TAG = RecipeActivity.class.getSimpleName();
    public static ArrayList<Recipe> mRecipes;
    public static final String RECIPE = "recipe";
    RecipeAdapter mAdapter;
    boolean isTablet;

    @BindView(R.id.recipe_recylcer_view) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.main) CoordinatorLayout layout;
    @BindView(R.id.progress_bar) ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        Timber.plant(new Timber.DebugTree());

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        mRecyclerView.setHasFixedSize(true);
        int column;
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if(isTablet)
            column = getResources().getInteger(R.integer.grid_column);
        else
            column = 0;

        if (savedInstanceState != null){
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPE);
            loadRecipes(column);
        }else {

            final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/" +
                    "59121517_baking/baking.json";

            if (isNetworkAvailable()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                loadRecipes(column);
                getRecipeData(url);
            } else {
                mRecyclerView.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(layout, "No Internet Connection," +
                        " Turn ON data and click RERTY", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int column;
                        if (isTablet)
                            column = getResources().getInteger(R.integer.grid_column);
                        else
                            column = 0;

                        loadRecipes(column);
                        getRecipeData(url);
                    }
                });
                snackbar.show();
            }
        }
    }


    public boolean isNetworkAvailable() {
        boolean status;
        try {
            ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connect.getActiveNetworkInfo();
            status = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public void getRecipeData(final String url) {
        progressbar.setVisibility(View.VISIBLE);

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Timber.i(TAG, "Network request successful");
                        Timber.i(TAG,"Response: = " + response);
                        try {
                            String jsonData = response.toString();
                            mRecipes = getBakingRecipe(jsonData);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressbar.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.steRecipesData(mRecipes);
                        Timber.i(TAG, "Adapter updated appropriately");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mRecyclerView.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(layout, "No Internet Connection," +
                        " Turn ON data and click RERTY", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int column = 0;
                        if (isTablet) {
                            column = getResources().getInteger(R.integer.grid_column);
                        }
                        loadRecipes(column);
                        getRecipeData(url);
                    }
                });
                snackbar.show();
                progressbar.setVisibility(View.INVISIBLE);
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

    private ArrayList<Recipe> getBakingRecipe(String jsonData) throws JSONException {
        JSONArray bakeArray = new JSONArray(jsonData);
        mRecipes = new ArrayList<>();
        for (int i = 0; i < bakeArray.length(); i++) {
            mRecipes.add(new Recipe(bakeArray.getJSONObject(i)));
        }

        return mRecipes;
    }

    private void loadRecipes(int column){
        RecyclerView.LayoutManager layoutManager;

        if (!isTablet) {
            layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            RecipeListActivity.mTwoPane = false;
        }else {
            layoutManager = new GridLayoutManager(this, column,
                    LinearLayoutManager.VERTICAL, false);
            RecipeListActivity.mTwoPane = true;
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecipeAdapter(mRecipes);
        mRecyclerView.setAdapter(mAdapter);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, mRecipes);
    }
}