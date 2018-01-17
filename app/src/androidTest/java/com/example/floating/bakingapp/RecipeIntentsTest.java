package com.example.floating.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.floating.bakingapp.recipes.RecipeActivity;
import com.example.floating.bakingapp.recipes.RecipeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Abdulkarim on 7/2/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeIntentsTest {

    @Rule
    public IntentsTestRule<RecipeActivity> mRecipeRule = new IntentsTestRule<RecipeActivity>(RecipeActivity.class);

    @Test
    public void recyclerIntentTest() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_recylcer_view)).perform(RecyclerViewActions
                .actionOnItemAtPosition(1, click()));

        intended(toPackage("com.kafilicious.abdul.bakingapp"));
    }

    @Test
    public void RecipeIntentComponentsTest() {

        String RECIPE_LIST = "recipeList";

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_recylcer_view)).perform(RecyclerViewActions
                .actionOnItemAtPosition(2, click()));
        intended(hasComponent(RecipeListActivity.class.getName()));
        intended(hasExtraWithKey(RECIPE_LIST));
    }
}
