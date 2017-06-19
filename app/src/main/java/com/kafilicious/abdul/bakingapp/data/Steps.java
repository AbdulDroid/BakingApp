package com.kafilicious.abdul.bakingapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Steps
{

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Steps(){

    }

    public Steps(JSONObject step_jason) {
        try {
            this.id = step_jason.getInt("id");
            this.shortDescription = step_jason.optString("shortDescription");
            this.description = step_jason.optString("description");
            this.videoURL = step_jason.optString("videoURL");
            this.thumbnailURL = step_jason.getString("thumbnailURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}

