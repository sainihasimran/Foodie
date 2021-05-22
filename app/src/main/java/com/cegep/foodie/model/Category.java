package com.cegep.foodie.model;

import com.cegep.foodie.R;

public enum Category {

    VEGETARIAN(R.drawable.vegetarian), NON_VEGETARIAN(R.drawable.meat), VEGAN(R.drawable.meat), DRINKS(R.drawable.drinks);

    public int drawable;

    Category(int drawable) {
        this.drawable = drawable;
    }

    public String getNiceName() {
        return this.toString().charAt(0) + this.toString().substring(1).toLowerCase().replace("_", " ");
    }
}
