package com.example.floating.bakingapp.recipes.adapter

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.model.Recipe
import com.example.floating.bakingapp.recipes.RecipeActivity
import com.example.floating.bakingapp.recipes.RecipeListActivity

import org.parceler.Parcels

import butterknife.BindView
import butterknife.ButterKnife

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

class RecipeAdapter() : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    private var context: Context? = null
    private val RECIPE_LIST = "recipeList"
    internal lateinit var recipeList: List<Recipe>

    fun steRecipesData(recipeData: List<Recipe>) {
        this.recipeList = recipeData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contextOne = parent.context
        val view: View
        view = LayoutInflater.from(contextOne).inflate(R.layout.recipe_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeAdapter.ViewHolder, position: Int) {
        val recipeName: String? = recipeList!![position].name
        val recipeServing: Int = recipeList!![position].servings
        val recipeImage: String? = recipeList!![position].image
        val servings: String
        servings = String.format(context!!.getString(R.string.servings_text), recipeServing)

        holder.recipeTextView!!.text = recipeName
        holder.servingTextView!!.text = servings
        Glide.with(context)
                .load(recipeImage)
                .placeholder(imageResource(position))
                .into(holder.recipeImageView!!)
    }

    override fun getItemCount() = recipeList.size

    private fun imageResource(index: Int): Int {
        val imageResource = intArrayOf(R.drawable.nutella_pie_one, R.drawable.brownies_one, R.drawable.yellow_cake_0, R.drawable.cheese_cake_one)
        return imageResource[index]
    }

    private fun setNotification(content: String, position: Int) {

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle("Viewed Recipes")
                .setContentText(content)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setCategory("alarm")
                .setLargeIcon(BitmapFactory.decodeResource(context!!.resources, R.drawable.nutella_pie_three))
                .setPriority(NotificationCompat.PRIORITY_LOW)
        val callingIntent = Intent(context, RecipeListActivity::class.java)
        val recipe = recipeList!![position]
        callingIntent.putExtra(RECIPE_LIST, Parcels.wrap(recipe))


        // This stack builder object will contain an artificial back stack for the started
        // activity. This ensures that navigating backwards from the activity leads out of the
        // calling application to the Home Screen.

        val stackBuilder = TaskStackBuilder.create(context!!)
        //This adds the back stack for the Intent (but not the intent itself)
        stackBuilder.addParentStack(RecipeActivity::class.java)
        //This line adds the Intent that starts the Activity to the top of the Stack
        stackBuilder.addNextIntent(callingIntent)
        val callingPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(callingPendingIntent)

        val notificationManager = context!!.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(RecipeActivity.NOTIFICATION_ID, mBuilder.build())
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        @BindView(R.id.recipe_name) internal lateinit var recipeTextView: TextView
        @BindView(R.id.number_of_servings) internal lateinit var servingTextView: TextView
        @BindView(R.id.recipe_image) internal lateinit var recipeImageView: ImageView

        init {
            ButterKnife.bind(this, view)
            view.isClickable = true
            view.setOnClickListener(this)
            context = view.context
        }

        override fun onClick(view: View) {

            val recipeName: String?
            val servings: String
            val position = adapterPosition
            recipeName = recipeList!![position].name
            servings = String.format(context!!.getString(R.string.servings_text),
                    recipeList!![position].servings)
            val recipe = recipeList!![position]
            val intent = Intent(context, RecipeListActivity::class.java)
            intent.putExtra(RECIPE_LIST, Parcels.wrap(recipe))
            setNotification(recipeName + "\n" + servings, position)
            context!!.startActivity(intent)
        }
    }


}