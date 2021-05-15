package com.cegep.foodie.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class PreparationStep implements Parcelable  {

    public int stepNumber;

    public String stepDescription;

    public PreparationStep() {

    }

    public PreparationStep(int stepNumber, String stepDescription) {
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
    }

    public PreparationStep(Parcel in) {
        stepNumber = in.readInt();
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
        dest.writeString(stepDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreparationStep that = (PreparationStep) o;
        return stepNumber == that.stepNumber &&
                Objects.equals(stepDescription, that.stepDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepNumber, stepDescription);
    }
}
