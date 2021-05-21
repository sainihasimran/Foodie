package com.cegep.foodie.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import java.util.Objects;

public class Recipe implements Parcelable {

    public String id;
    private String name;


    private String image;

    private int servingSize;

    private String category;

    private int duration;

    private int calories;

    private List<Ingredient> ingredients;

    private List<PreparationStep> preparationSteps;



    public Recipe(String id, String name, String image, int servingSize, String category, int duration, int calories, List<Ingredient> ingredients, List<PreparationStep> preparationSteps) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.servingSize = servingSize;
        this.category = category;
        this.duration = duration;
        this.calories = calories;
        this.ingredients = ingredients;
        this.preparationSteps = preparationSteps;
    }


    public Recipe() {

    }


    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        servingSize = in.readInt();
        category = in.readString();
        duration = in.readInt();
        calories = in.readInt();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        preparationSteps = in.createTypedArrayList(PreparationStep.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<PreparationStep> getPreparationSteps() {
        return preparationSteps;
    }

    public void setPreparationSteps(List<PreparationStep> preparationSteps) {
        this.preparationSteps = preparationSteps;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(servingSize);
        dest.writeString(category);
        dest.writeInt(duration);
        dest.writeInt(calories);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(preparationSteps);
    }
}
