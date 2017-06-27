package com.example.floating.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Steps;
import com.example.floating.bakingapp.fragments.RecipeDetailsFragment;
import com.example.floating.bakingapp.ui.RecipeDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private ArrayList<Steps> mSteps;
    private Context context;
    private boolean twoPanes;
    public static final String STEPS = "steps";
    private View mSelectedV;
    String STEP_ITEM = "step_item";

    public OnStepItemClickListener mItemClickListener;

    public void setOnStepItemClickListener(OnStepItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnStepItemClickListener{
        void onStepItemClickListener(View view, int position);
    }

    public RecipeStepsAdapter(ArrayList<Steps> stepsList, boolean twoPanes){
        this.mSteps = stepsList;
        this.twoPanes = twoPanes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.steps_list_content,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.recipeStep = mSteps.get(position);
        if (holder.getAdapterPosition() == 0){
            holder.stepTextView.setText("");
        }else {
            holder.stepTextView.setText(String.format(context.getResources()
            .getString(R.string.step_text), mSteps.get(position).getId()));
        }
        holder.descriptionTextView.setText(mSteps.get(position).getShortDescription());

        if (mSteps.get(position).getVideoURL().isEmpty()){
            Glide.with(context)
                    .load(mSteps.get(position).getThumbnailURL())
                    .placeholder(R.drawable.ic_videocam_off_white_24dp)
                    .into(holder.imageImageView);
        }else{
            Glide.with(context)
                    .load(mSteps.get(position).getThumbnailURL())
                    .placeholder(R.drawable.ic_videocam_white_24dp)
                    .into(holder.imageImageView);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Steps step = mSteps.get(getA)
                if (twoPanes){
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeDetailsFragment.ITEM_ID, position);
                    arguments.putBoolean(RecipeDetailsFragment.PANES, twoPanes);
                    arguments.putParcelableArrayList(RecipeDetailsFragment.STEPS, mSteps);
                    RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_details_container, fragment)
                            .commit();
                }else{
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailsActivity.class);
                    intent.putExtra(RecipeDetailsFragment.ITEM_ID, holder.getAdapterPosition());
                    intent.putParcelableArrayListExtra(STEPS, mSteps);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View mView;
        @BindView(R.id.step_id)
        TextView stepTextView;
        @BindView(R.id.short_description)
        TextView descriptionTextView;
        @BindView(R.id.recipe_step_image)
        ImageView imageImageView;
        public Steps recipeStep;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setClickable(true);
            mView = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

//            Steps step = mSteps.get(getAdapterPosition());
//
//            Bundle stepArguments = new Bundle();
//
//            stepArguments.putParcelable(STEP_ITEM,step);
//            ((RecipeListActivity)context).onRecipeStepSelected(stepArguments, getAdapterPosition());
            int mSelectedPos = 0;
            if (mItemClickListener != null)
                mItemClickListener.onStepItemClickListener(view, getAdapterPosition());
            if (!view.isSelected()){
                //We are selecting the view clicked
                if (mSelectedV != null){
                    //deselect the previously selected view
                    mSelectedV.setSelected(false);
                }
                mSelectedPos = this.getAdapterPosition();
                mSelectedV = view;
            }else {
                //We are deselecting the view clicked
                mSelectedPos = -1;
                mSelectedV = null;
            }
            //toggle the item clicked
            view.setSelected(!view.isSelected());
        }
    }
}