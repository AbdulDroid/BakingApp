package com.example.floating.bakingapp.details.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.example.floating.bakingapp.R
import com.example.floating.bakingapp.model.Steps
import com.example.floating.bakingapp.details.fragment.RecipeDetailsFragment
import com.example.floating.bakingapp.details.RecipeDetailsActivity

import org.parceler.Parcels

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

class RecipeStepsAdapter(private val mSteps: ArrayList<Steps>?, private val twoPanes: Boolean,
                         private val title: String) : RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder>() {
    private var context: Context? = null
    private var selected_position: Int = 0

    fun getposition(): Int {
        return selected_position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.steps_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (selected_position == position) {
            holder.mView.setBackgroundColor(context!!.resources.getColor(R.color.colorPrimaryDark))
        } else {
            holder.mView.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.recipeStep = mSteps!![position]
        if (holder.adapterPosition == 0) {
            holder.stepTextView!!.text = ""
        } else {
            holder.stepTextView!!.text = String.format(context!!.resources
                    .getString(R.string.step_text), mSteps[position].id)
        }
        holder.descriptionTextView!!.text = mSteps[position].shortDescription

        if (mSteps[position].videoURL!!.isEmpty()) {
            Glide.with(context)
                    .load(mSteps[position].thumbnailURL)
                    .placeholder(R.drawable.ic_videocam_off_white_24dp)
                    .into(holder.imageImageView!!)
        } else {
            Glide.with(context)
                    .load(mSteps[position].thumbnailURL)
                    .placeholder(R.drawable.ic_videocam_white_24dp)
                    .into(holder.imageImageView!!)
        }

        holder.mView.setOnClickListener { view ->
            //                Steps step = mSteps.get(getA)
            notifyItemChanged(selected_position)
            selected_position = position
            notifyItemChanged(selected_position)

            if (twoPanes) {
                val arguments = Bundle()
                arguments.putInt(RecipeDetailsFragment.ITEM_ID, holder.adapterPosition)
                arguments.putBoolean(RecipeDetailsFragment.PANES, twoPanes)
                arguments.putParcelable(RecipeDetailsFragment.STEPS, Parcels.wrap(mSteps))
                val fragment = RecipeDetailsFragment()
                fragment.arguments = arguments
                (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.recipe_step_details_container, fragment)
                        .commit()
            } else {
                val context = view.context
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                intent.putExtra(RecipeDetailsFragment.ITEM_ID, holder.adapterPosition)
                intent.putExtra(STEPS, Parcels.wrap(mSteps))
                intent.putExtra(RECIPE_TITLE, title)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return mSteps?.size ?: 0
    }

    inner class ViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {
        @BindView(R.id.step_id)
        lateinit var stepTextView: TextView
        @BindView(R.id.short_description)
        lateinit var descriptionTextView: TextView
        @BindView(R.id.recipe_step_image)
        lateinit var imageImageView: ImageView
        var recipeStep: Steps? = null

        init {
            ButterKnife.bind(this, mView)
            mView.isClickable = true
        }
    }

    companion object {
        private val RECIPE_TITLE = "title"
        private val STEPS = "steps"
    }
}