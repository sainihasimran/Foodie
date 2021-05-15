package com.cegep.foodie.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import java.util.Objects;

public class Recipe implements Parcelable {

    public int id;

    private String name;

    private String image;

    private int servingSize;

    private String category;

    private int duration;

    private int calories;

    private List<Ingredient> ingredients;

    private List<PreparationStep> preparationSteps;

    public Recipe() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage() {
        // TODO: 14/05/21 Add default image
        return image;
    }

    private Recipe(Parcel in) {
        image = in.readString();
        name = in.readString();
        servingSize = in.readInt();
        category = in.readString();
        duration = in.readInt();
        calories = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeInt(servingSize);
        dest.writeString(category);
        dest.writeInt(duration);
        dest.writeInt(calories);
    }

    public static final Creator<Recipe> CREATOR
            = new Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return id == recipe.id &&
                servingSize == recipe.servingSize &&
                duration == recipe.duration &&
                calories == recipe.calories &&
                Objects.equals(name, recipe.name) &&
                Objects.equals(image, recipe.image) &&
                Objects.equals(category, recipe.category) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(preparationSteps, recipe.preparationSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, servingSize, category, duration, calories, ingredients, preparationSteps);
    }
}
