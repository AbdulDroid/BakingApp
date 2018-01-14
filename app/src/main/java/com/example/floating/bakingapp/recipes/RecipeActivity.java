package com.example.floating.bakingapp.recipes;

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

import com.example.floating.bakingapp.AppController;
import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.recipes.adapter.RecipeAdapter;
import com.example.floating.bakingapp.model.Recipe;

import org.parceler.Parcels;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity implements RecipeContract.View{

    public static final String TAG = RecipeActivity.class.getSimpleName();
    public static final String RECIPE = "recipe";
    public static final int NOTIFICATION_ID = 10;
    public static ArrayList<Recipe> mRecipes;
    RecipeAdapter mAdapter;
    boolean isTablet;

    @BindView(R.id.recipe_recylcer_view) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_main) Toolbar toolbar;
    @BindView(R.id.main) CoordinatorLayout layout;
    @BindView(R.id.progress_bar) ProgressBar progressbar;
    Unbinder unbinder;

    @Inject
    RecipePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        unbinder = ButterKnife.bind(this);

        ((AppController) getApplication())
                .getAppComponent()
                .add(new RecipeModule(this))
                .inject(this);
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
            mRecipes = Parcels.unwrap(savedInstanceState.getParcelable(RECIPE));
            loadRecipes(column);
        }else {

            if (isNetworkAvailable()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                loadRecipes(column);
                progressbar.setVisibility(View.VISIBLE);
                presenter.getRecipes();
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
                        progressbar.setVisibility(View.VISIBLE);
                        presenter.getRecipes();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
        outState.putParcelable(RECIPE, Parcels.wrap(mRecipes));
    }

    @Override
    public void onGetRecipeSuccess(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        progressbar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.steRecipesData(mRecipes);
        Timber.i(TAG, "Adapter updated appropriately");
    }

    @Override
    public void onGetRecipeError(String error) {

    }
}