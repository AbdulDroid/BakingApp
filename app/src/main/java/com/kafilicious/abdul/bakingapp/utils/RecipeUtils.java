package com.kafilicious.abdul.bakingapp.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Abdulkarim on 6/12/2017.
 */

public class RecipeUtils {

    public static final String BASE_REQUEST_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017";
    public static final String JSON_REQUEST_URL =
            "/May/59121517_baking/baking.json";

    public static URL buildJsonUrl(){
        Uri build = Uri.parse(BASE_REQUEST_URL).buildUpon()
                .appendPath(JSON_REQUEST_URL).build();
        URL url = null;
        try{
            url = new URL(build.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
}
