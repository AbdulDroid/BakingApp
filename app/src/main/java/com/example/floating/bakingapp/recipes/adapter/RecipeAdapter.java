package com.example.floating.bakingapp.recipes.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.model.Recipe;
import com.example.floating.bakingapp.recipes.RecipeActivity;
import com.example.floating.bakingapp.recipes.RecipeListActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.floating.bakingapp.recipes.RecipeActivity.NOTIFICATION_ID;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    String RECIPE_NAME = "recipe_name";
    String RECIPE_STEPS_LIST = "recipe_steps";
    String RECIPE_INDEX = "index";
    private List<Recipe> recipe_list;
    private Context context;
    private String RECIPE_LIST = "recipe_list";
    public RecipeAdapter(List<Recipe> recipes){
        this.recipe_list = recipes;
    }

    public void steRecipesData(List<Recipe> recipeData){
        this.recipe_list = recipeData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context contextOne = parent.getContext();
        View view;
        view = LayoutInflater.from(contextOne).inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        String recipeName = null;
        int recipeServing ;
        String recipeImage = null;
        String servings = null;
        recipeName = recipe_list.get(position).getName();
        recipeServing = recipe_list.get(position).getServings();
        recipeImage = recipe_list.get(position).getImage();
        servings = String.format(context.getString(R.string.servings_text), recipeServing);

        holder.recipeTextView.setText(recipeName);
        holder.servingTextView.setText(servings);
        Glide.with(context)
                .load(recipeImage)
                .placeholder(imageResource(position))
                .into(holder.recipeImageView);
    }

    @Override
    public int getItemCount() {
        if (recipe_list == null)return 0;
        return recipe_list.size();
    }

    private int imageResource(int index) {
        int[] imageResource ={R.drawable.nutella_pie_one, R.drawable.brownies_one,
                R.drawable.yellow_cake_0, R.drawable.cheese_cake_one};
        return imageResource[index];
    }

    private void setNotification(String content, int position) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_menu_info_details)
                        .setContentTitle("Viewed Recipes")
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setCategory("alarm")
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.nutella_pie_three))
                        .setPriority(NotificationCompat.PRIORITY_LOW);
        Intent callingIntent = new Intent(context, RecipeListActivity.class);
        Recipe recipe = recipe_list.get(position);
        callingIntent.putExtra(RECIPE_LIST, Parcels.wrap(recipe));


        // This stack builder object will contain an artificial back stack for the started
        // activity. This ensures that navigating backwards from the activity leads out of the
        // calling application to the Home Screen.

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //This adds the back stack for the Intent (but not the intent itself)
        stackBuilder.addParentStack(RecipeActivity.class);
        //This line adds the Intent that starts the Activity to the top of the Stack
        stackBuilder.addNextIntent(callingIntent);
        PendingIntent callingPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(callingPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_name) TextView recipeTextView;
        @BindView(R.id.number_of_servings) TextView servingTextView;
        @BindView(R.id.recipe_image) ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setClickable(true);
            view.setOnClickListener(this);
            context = view.getContext();
        }

        @Override
        public void onClick(View view) {

            String recipeName = null;
            String servings = null;
            int position = getAdapterPosition();
            recipeName = recipe_list.get(position).getName();
            servings = String.format(context.getString(R.string.servings_text),
                    recipe_list.get(position).getServings());
            Recipe recipe = recipe_list.get(position);
            Intent intent = new Intent(context, RecipeListActivity.class);
            intent.putExtra(RECIPE_LIST, Parcels.wrap(recipe));
            setNotification(recipeName + "\n" + servings, position);
            context.startActivity(intent);
        }
    }


}