package com.example.floating.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.ui.RecipeDetailsActivity;
import com.example.floating.bakingapp.data.Steps;
import com.example.floating.bakingapp.fragments.RecipeDetailsFragment;

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
    private int mSelectedPos;
    private View mSelectedV;

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
        holder.stepTextView.setText(String.format(context.getResources()
                .getString(R.string.step_text), mSteps.get(position).getId() + 1));
        holder.descriptionTextView.setText(mSteps.get(position).getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (twoPanes){
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeDetailsFragment.ITEM_ID, position);
                    arguments.putBoolean(RecipeDetailsFragment.PANES, twoPanes);
                    RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_details_container, fragment)
                            .commit();
                }else{
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailsActivity.class);
                    intent.putExtra(RecipeDetailsFragment.ITEM_ID, holder.recipeStep.getId());
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
        @BindView(R.id.step_id) TextView stepTextView;
        @BindView(R.id.short_description) TextView descriptionTextView;
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