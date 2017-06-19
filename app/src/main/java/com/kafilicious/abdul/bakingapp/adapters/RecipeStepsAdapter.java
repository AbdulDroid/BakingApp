package com.kafilicious.abdul.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kafilicious.abdul.bakingapp.R;
import com.kafilicious.abdul.bakingapp.data.Steps;
import com.kafilicious.abdul.bakingapp.fragments.RecipeDetailsFragment;
import com.kafilicious.abdul.bakingapp.ui.RecipeDetailsActivity;

import java.util.ArrayList;

/**
 * Created by Abdulkarim on 6/12/2017.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private ArrayList<Steps> mSteps;
    private Context context;
    private boolean twoPanes;

    public RecipeStepsAdapter(ArrayList<Steps> stepsList, boolean twoPanes){
        this.mSteps = stepsList;
        this.twoPanes = twoPanes;
    }

    public void steRecipesData(ArrayList<Steps> recipeData){
        this.mSteps = recipeData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.steps_list_content,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.recipeStep = mSteps.get(position);
        holder.stepTextView.setText(String.format(context.getResources()
                .getString(R.string.step_text), mSteps.get(position).getId() + 1));
        holder.descriptionTextView.setText(mSteps.get(position).getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (twoPanes){
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeDetailsFragment.ARG_ITEM_ID, getAdapterposition());
                    RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                }else{
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailsActivity.class);
                    intent.putExtra(RecipeDetailsFragment.ARG_ITEM_ID, holder.recipeStep.getId());
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
        public TextView stepTextView, descriptionTextView;
        public Steps recipeStep;
        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            mView = view;
            view.setOnClickListener(this);
            stepTextView = (TextView) view.findViewById(R.id.step_id);
            descriptionTextView = (TextView) view.findViewById(R.id.short_description);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
