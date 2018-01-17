package com.example.floating.bakingapp.recipes

import android.content.ContentValues
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.TextView

import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.details.adapter.RecipeStepsAdapter
import com.example.floating.bakingapp.model.Ingredients
import com.example.floating.bakingapp.model.Recipe
import com.example.floating.bakingapp.model.Steps
import com.example.floating.bakingapp.database.RecipeContract
import com.example.floating.bakingapp.database.RecipeDbHelper
import com.example.floating.bakingapp.details.fragment.RecipeDetailsFragment
import com.example.floating.bakingapp.utils.RecipeUtils

import org.parceler.Parcels

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import timber.log.Timber

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

class RecipeListActivity : AppCompatActivity() {
    private var layoutManager: LinearLayoutManager? = null
    internal var STEP_ITEM = "step_item"
    private var stepsListState: Parcelable? = null
    private var mPosition = -1
    private var topView: Int = 0
    private var position: Int = 0
    private var recipe: Recipe? = null
    private lateinit var adapter: RecipeStepsAdapter
    private var provider: RecipeDbHelper? = null
    internal var title: String? = null

    @BindView(R.id.recipe_ingredients_tv) lateinit var ingredientTextView: TextView
    @BindView(R.id.recipe_steps_rv) lateinit var stepsRecyclerView: RecyclerView
    @BindView(R.id.toolbar_list) lateinit var toolbar: Toolbar

    private val isLastViewed: Int
        get() {
            val size = provider!!.getRecipeDBSize(0)
            return if (size == 0)
                0
            else
                1
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
        ButterKnife.bind(this)
        Timber.plant(Timber.DebugTree())
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        provider = RecipeDbHelper(this)


        val intent = intent
        if (intent != null && intent.hasExtra(RECIPE_LIST)) {

            recipe = Parcels.unwrap(intent.getParcelableExtra(RECIPE_LIST))
            steps = ArrayList(recipe!!.steps!!)
            ingredientsList = ArrayList(recipe!!.ingredients!!)
            title = recipe!!.name

            if (title != null)
                actionBar!!.title = title

            val values = ContentValues()

            values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, recipe!!.name)
            values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_LIST, recipe!!.ingredient_string)

            if (isLastViewed == 1) {
                provider!!.updateRecipe(recipe!!)
            } else {
                provider!!.addViewedRecipe(recipe!!)
            }

            if (mTwoPane) {
                val arguments = Bundle()
                arguments.putInt(RecipeDetailsFragment.ITEM_ID, 0)
                arguments.putBoolean(RecipeDetailsFragment.PANES, mTwoPane)
                val fragment = RecipeDetailsFragment()
                fragment.arguments = arguments
                supportFragmentManager.beginTransaction()
                        .replace(R.id.recipe_step_details_container, fragment)
                        .commit()
            }
        }

        if (findViewById<View>(R.id.recipe_step_details_container) != null) {
            Timber.e("ListActivity", "containerFound")
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true

            if (savedInstanceState == null) {
                val arguments = Bundle()
                arguments.putInt(RecipeDetailsFragment.ITEM_ID, 0)
                arguments.putBoolean(RecipeDetailsFragment.PANES, mTwoPane)
                val fragment = RecipeDetailsFragment()
                fragment.arguments = arguments
                supportFragmentManager.beginTransaction()
                        .replace(R.id.recipe_step_details_container, fragment)
                        .commit()
            }
        } else {
            mTwoPane = false
            Timber.e("ListActivity", "containerNotFound")
        }



        stepsRecyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        stepsRecyclerView!!.layoutManager = layoutManager
        adapter = RecipeStepsAdapter(steps, mTwoPane, this.title!!)
        stepsRecyclerView!!.itemAnimator = DefaultItemAnimator()
        stepsRecyclerView!!.adapter = adapter
        stepsRecyclerView!!.isNestedScrollingEnabled = false
        adapter.notifyDataSetChanged()

        ingredientTextView!!.text = RecipeUtils.getIngredientsString(ingredientsList)

    }

    override fun onPause() {
        super.onPause()

        stepsListState = layoutManager!!.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        if (stepsListState != null)
            layoutManager!!.onRestoreInstanceState(stepsListState)
        if (mPosition != -1) {
            layoutManager!!.scrollToPositionWithOffset(mPosition, topView)
            layoutManager!!.scrollToPosition(position)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putParcelable(RECIPE_LIST, Parcels.wrap(recipe))
        stepsListState = layoutManager!!.onSaveInstanceState()

        outState.putParcelable(STEPS_LIST_KEY, stepsListState)
    }

    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)

        recipe = Parcels.unwrap(state.getParcelable(RECIPE_LIST))
        //steps = recipe.getSteps();
        //ingredientsList = recipe.getIngredients();
        stepsListState = state.getParcelable(STEPS_LIST_KEY)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    companion object {

        /**
         * Whether or not the activity is in two-pane mode, i.e. running on a
         * tablet device.
         */
        var mTwoPane: Boolean = false
        lateinit var steps: ArrayList<Steps>
        lateinit var ingredientsList: ArrayList<Ingredients>
        val STEPS_LIST_KEY = "recipe_steps"
        val RECIPE_LIST = "recipeList"
        var index: Int? = null
    }
}
