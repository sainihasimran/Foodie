package com.cegep.foodie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PreparationStep implements Parcelable  {

    public int stepNumber;

    public int recipeId;

    public String stepDescription;

    public PreparationStep() {

    }

    public PreparationStep(int stepNumber, int recipeId, String stepDescription) {
        this.stepNumber = stepNumber;
        this.recipeId = recipeId;
        this.stepDescription = stepDescription;
    }

    public PreparationStep(Parcel in) {
        stepNumber = in.readInt();
        recipeId = in.readInt();
        stepDescription = in.readString();
    }

    public static final Creator<PreparationStep> CREATOR
            = new Creator<PreparationStep>() {
        public PreparationStep createFromParcel(Parcel in) {
            return new PreparationStep(in);
        }

        public PreparationStep[] newArray(int size) {
            return new PreparationStep[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepNumber);
        dest.writeInt(recipeId);
        dest.writeString(stepDescription);
    }
}
