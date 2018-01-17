package com.example.floating.bakingapp.details

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast

import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.model.Steps
import com.example.floating.bakingapp.details.fragment.RecipeDetailsFragment
import com.example.floating.bakingapp.recipes.RecipeListActivity

import org.parceler.Parcels

import java.util.ArrayList

import timber.log.Timber

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Parcelable
import com.example.floating.bakingapp.details.fragment.RecipeDetailsFragment.Companion.ITEM_ID

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

class RecipeDetailsActivity : AppCompatActivity() {
    private var steps: ArrayList<Steps>? = null
    private var title: String? = null
    private var fab: FloatingActionButton? = null
    private var fab1: FloatingActionButton? = null
    private var x1 = 0f
    private var x2: Float = 0.toFloat()
    private var deltaX: Float = 0.toFloat()
    internal var orientation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)//Remove titlebar
            this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        setContentView(R.layout.activity_recipe_detail)

        if (orientation == ORIENTATION_PORTRAIT) {
            fab = findViewById(R.id.fab)
            fab1 = findViewById(R.id.fab1)
            val toolbar = findViewById<Toolbar>(R.id.detail_toolbar)
            setSupportActionBar(toolbar)

            if (supportActionBar != null) {
                supportActionBar!!.setHomeButtonEnabled(true)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }

            if (savedInstanceState != null && savedInstanceState.containsKey(STEP_INDEX)) {
                index = savedInstanceState.getInt(STEP_INDEX)
                steps = Parcels.unwrap<ArrayList<Steps>>(savedInstanceState.getParcelable<Parcelable>(RECIPE_STEPS))
                title = savedInstanceState.getString(TITLE)
                supportActionBar!!.setTitle(title)
            } else {
                val intent = this.intent

                if (intent != null && intent.hasExtra(RecipeDetailsFragment.ITEM_ID)) {
                    index = intent.getIntExtra(RecipeDetailsFragment.ITEM_ID, 0)
                    steps = Parcels.unwrap<ArrayList<Steps>>(intent.getParcelableExtra<Parcelable>(STEPS))
                    title = intent.getStringExtra(TITLE)
                    supportActionBar!!.setTitle(title)
                }
            }
            fab!!.setOnClickListener {
                if (index > 0 && index < steps!!.size - 1) {
                    --index
                    if (index < 0)
                        index = 0

                    fragmentTransaction(index)
                    Timber.e(RecipeDetailsActivity::class.java.simpleName, index.toString())
                } else {
                    if (index == steps!!.size - 1) {
                        index = steps!!.size - 2
                        fragmentTransaction(index)
                    } else {
                        index = 0
                        Toast.makeText(applicationContext, "This is the first step",
                                Toast.LENGTH_LONG).show()
                    }
                }
            }

            fab1!!.setOnClickListener {
                if (index < steps!!.size - 1) {
                    ++index
                    if (index > steps!!.size - 1)
                        index = steps!!.size - 1

                    fragmentTransaction(index)
                } else {
                    Toast.makeText(applicationContext, "This is the last Step",
                            Toast.LENGTH_LONG).show()
                    index = steps!!.size - 1
                }
            }
        } else {
            if (savedInstanceState != null && savedInstanceState.containsKey(STEP_INDEX)) {
                index = savedInstanceState.getInt(STEP_INDEX)
                steps = Parcels.unwrap<ArrayList<Steps>>(savedInstanceState.getParcelable<Parcelable>(RECIPE_STEPS))
                title = savedInstanceState.getString(TITLE)
                /*getSupportActionBar().setTitle(title);*/
            } else {
                val intent = this.intent

                if (intent != null && intent.hasExtra(RecipeDetailsFragment.ITEM_ID)) {
                    index = intent.getIntExtra(RecipeDetailsFragment.ITEM_ID, 0)
                    steps = Parcels.unwrap<ArrayList<Steps>>(intent.getParcelableExtra<Parcelable>(STEPS))
                    title = intent.getStringExtra(TITLE)
                    /* getSupportActionBar().setTitle(title);*/
                }
            }
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
            fragmentTransaction(intent.getIntExtra(ITEM_ID, 0))
        }
    }

    fun fragmentTransaction(index: Int) {
        showButtons(index)
        val arguments = Bundle()
        arguments.putInt(ITEM_ID, index)
        arguments.putParcelable(RECIPE_STEPS, Parcels.wrap<ArrayList<Steps>>(steps))
        val fragment = RecipeDetailsFragment()
        fragment.arguments = arguments
        supportFragmentManager.beginTransaction()
                .replace(R.id.recipe_step_details_container, fragment)
                .commit()
    }

    private fun showButtons(index: Int) {
        if (orientation == ORIENTATION_PORTRAIT) {
            if (index <= 0)
                fab!!.visibility = View.GONE
            else
                fab!!.visibility = View.VISIBLE
            if (index >= steps!!.size - 1)
                fab1!!.visibility = View.GONE
            else
                fab1!!.visibility = View.VISIBLE
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x
            MotionEvent.ACTION_UP -> {
                x2 = event.x

                deltaX = x2 - x1

                if (Math.abs(deltaX) >= MIN_DISTANCE) {
                    if (x2 > x1) {
                        if (index > 0 && index < steps!!.size - 1) {
                            --index
                            if (index < 0)
                                index = 0

                            fragmentTransaction(index)
                            Timber.e(RecipeDetailsActivity::class.java.simpleName, index.toString())
                        } else {
                            if (index == steps!!.size - 1) {
                                index = steps!!.size - 2
                                fragmentTransaction(index)
                            } else {
                                index = 0
                                Toast.makeText(applicationContext, "This is the first step",
                                        Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        if (index < steps!!.size - 1) {
                            ++index
                            if (index > steps!!.size - 1)
                                index = steps!!.size - 1

                            fragmentTransaction(index)
                        } else {
                            Toast.makeText(applicationContext, "This is the last Step",
                                    Toast.LENGTH_LONG).show()
                            index = steps!!.size - 1
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                navigateUpTo(Intent(this, RecipeListActivity::class.java))
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt("orientation", orientation)
        outState.putInt(STEP_INDEX, index)
        outState.putParcelable(RECIPE_STEPS, Parcels.wrap<ArrayList<Steps>>(steps))
        outState.putString(TITLE, title)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        index = savedInstanceState.getInt(STEP_INDEX)
        steps = Parcels.unwrap<ArrayList<Steps>>(savedInstanceState.getParcelable<Parcelable>(RECIPE_STEPS))
        title = savedInstanceState.getString(TITLE)
        /*getSupportActionBar().setTitle(title);*/
        super.onRestoreInstanceState(savedInstanceState)
    }

    companion object {

        private var index = 0
        val STEP_INDEX = "index"
        val RECIPE_STEPS = "recipe_steps"
        val TITLE = "title"
        val STEPS = "steps"
        val MIN_DISTANCE = 150
    }
}
