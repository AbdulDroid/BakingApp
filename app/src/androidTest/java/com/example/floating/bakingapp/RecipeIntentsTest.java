package com.example.floating.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.floating.bakingapp.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola 2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeIntentsTest {
    @Rule
    IntentsTestRule<RecipeActivity> intentsTestRule = new IntentsTestRule<>(RecipeActivity.class);

    @Test
    public void checkIntentSent() {
        String mPackage = "com.example.floating.bakingapp.ui";

        //Checking Intents whether or not they match with the package name
        onView(withId(R.id.recipe_recylcer_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(toPackage(mPackage));
    }
}
