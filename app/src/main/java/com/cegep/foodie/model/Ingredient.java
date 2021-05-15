package com.cegep.foodie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    public String name;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    private Ingredient(Parcel in) {
        in.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        name = dest.readString();
    }

    public static final Creator<Ingredient> CREATOR
            = new Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
