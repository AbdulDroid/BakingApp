package com.kafilicious.abdul.bakingapp.ui;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kafilicious.abdul.bakingapp.AppController;
import com.kafilicious.abdul.bakingapp.R;
import com.kafilicious.abdul.bakingapp.adapters.RecipeAdapter;
import com.kafilicious.abdul.bakingapp.data.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Recipe> mRecipes;
    RecipeAdapter mAdapter;
    CoordinatorLayout layout;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        layout = (CoordinatorLayout) findViewById(R.id.main);
        progressbar = (ProgressBar) findViewById(R.id.progress_bar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_recylcer_view);
        mRecyclerView.setHasFixedSize(true);
        int column = getResources().getInteger(R.integer.grid_column);
        if (getSmallestWidth() < 600)
            layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
        else
            layoutManager = new GridLayoutManager(this, column,
                    LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecipeAdapter(mRecipes);
        mRecyclerView.setAdapter(mAdapter);

        final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/" +
                "59121517_baking/baking.json";

        if (isNetworkAvailable()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            getRecipeData(url);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar.make(layout, "No Internet Connection," +
                    " Turn ON data and click RERTY", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRecipeData(url);
                }
            });
            snackbar.show();
        }
    }


    public float getSmallestWidth() {
        //First we create a DisplayMetrics object
        DisplayMetrics dm = new DisplayMetrics();

        //Then pass in the default display details to the display metrics we created
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Then we can get the absolute value of the width and the height in pixels
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        //Then we can get the density of the pixels per inch on the device and use it as a scale factor
        float scaleFactor = dm.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        //returns the minimum screen width at all times
        return Math.min(widthDp, heightDp);
    }

    public boolean isNetworkAvailable() {
        boolean status = false;
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

    public void getRecipeData(String url) {
            progressbar.setVisibility(View.VISIBLE);

            JsonArrayRequest req = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Network request successful");
                            Log.i(TAG,"Response: = " + response);
                            try {
                                String jsonData = response.toString();
                                mRecipes = getBakingRecipe(jsonData);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            progressbar.setVisibility(View.INVISIBLE);
                            mAdapter.steRecipesData(mRecipes);
                            Log.i(TAG, "Adapter updated appropriately");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(),
                            Toast.LENGTH_LONG).show();
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

}
