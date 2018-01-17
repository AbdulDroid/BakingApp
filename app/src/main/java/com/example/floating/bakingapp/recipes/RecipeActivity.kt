package com.example.floating.bakingapp.recipes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar

import com.example.floating.bakingapp.AppController
import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.recipes.adapter.RecipeAdapter
import com.example.floating.bakingapp.model.Recipe

import org.parceler.Parcels

import java.util.ArrayList

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import timber.log.Timber

class RecipeActivity : AppCompatActivity(), RecipeContract.View {
    private lateinit var mAdapter: RecipeAdapter
    private var isTablet: Boolean = false
    private var column: Int = 0

    @BindView(R.id.recipe_recylcer_view) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.toolbar_main) lateinit var toolbar: Toolbar
    @BindView(R.id.main) lateinit var layout: CoordinatorLayout
    @BindView(R.id.progress_bar) lateinit var progressbar: ProgressBar
    private lateinit var unbinder: Unbinder

    @Inject lateinit var presenter: RecipePresenter


    private val isNetworkAvailable: Boolean
        get() {
            val status: Boolean
            try {
                val connect = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetInfo = connect.activeNetworkInfo
                status = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

            return status
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        unbinder = ButterKnife.bind(this)

        (application as AppController)
                .appComponent
                ?.add(RecipeModule(this))
                ?.inject(this)
        Timber.plant(Timber.DebugTree())

        toolbar!!.title = resources.getString(R.string.app_name)
        setSupportActionBar(toolbar)
        mRecyclerView!!.setHasFixedSize(true)
        isTablet = resources.getBoolean(R.bool.isTablet)
        if (isTablet)
            column = resources.getInteger(R.integer.grid_column)
        else
            column = 0

        if (savedInstanceState != null) {
            mRecipes = Parcels.unwrap(savedInstanceState.getParcelable<Parcelable>(RECIPE))
            loadRecipes(column)
        } else {

            if (isNetworkAvailable) {
                mRecyclerView!!.visibility = View.VISIBLE
                progressbar!!.visibility = View.VISIBLE
                presenter!!.getRecipes()
            } else {
                mRecyclerView!!.visibility = View.INVISIBLE
                val snackbar = Snackbar.make(layout!!, "No Internet Connection," +
                        " Turn ON data and click RERTY", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("RETRY") {
                    val column: Int = if (isTablet)
                        resources.getInteger(R.integer.grid_column)
                    else
                        0

                    progressbar!!.visibility = View.VISIBLE
                    presenter!!.getRecipes()
                }
                snackbar.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    private fun loadRecipes(column: Int) {
        val layoutManager: RecyclerView.LayoutManager

        if (!isTablet) {
            layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false)
            RecipeListActivity.mTwoPane = false
        } else {
            layoutManager = GridLayoutManager(this, column,
                    LinearLayoutManager.VERTICAL, false)
            RecipeListActivity.mTwoPane = true
        }

        mRecyclerView!!.layoutManager = layoutManager
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        mAdapter = RecipeAdapter()
        mRecyclerView!!.adapter = mAdapter

        mAdapter.steRecipesData(mRecipes)
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelable(RECIPE, Parcels.wrap(mRecipes))
    }

    override fun onGetRecipeSuccess(recipes: ArrayList<Recipe>) {
        mRecipes = recipes
        progressbar!!.visibility = View.INVISIBLE
        mRecyclerView!!.visibility = View.VISIBLE
        Timber.i(TAG, "Adapter updated appropriately")
        loadRecipes(column)
    }

    override fun onGetRecipeError(error: String) {

    }

    companion object {

        val TAG = RecipeActivity::class.java.simpleName!!
        val RECIPE = "recipe"
        val NOTIFICATION_ID = 10
        lateinit var mRecipes: ArrayList<Recipe>
    }
}