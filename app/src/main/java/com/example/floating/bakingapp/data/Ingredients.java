package com.example.floating.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class Ingredients implements Parcelable {
    public static final Parcelable.Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel parcel) {
            return new Ingredients(parcel);
        }

        @Override
        public Ingredients[] newArray(int i) {
            return new Ingredients[i];
        }
    };

    @SerializedName("quantity")
    private double quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    public Ingredients(){
    }

    protected Ingredients(Parcel in){
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public Ingredients(JSONObject ingredient_json) {
        try {
            this.quantity = ingredient_json.getDouble("quantity");
            this.measure = ingredient_json.optString("measure");
            this.ingredient = ingredient_json.optString("ingredient");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.quantity);
        parcel.writeString(this.measure);
        parcel.writeString(this.ingredient);
    }
}
