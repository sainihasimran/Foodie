package com.cegep.foodie.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.cegep.foodie.R;
import com.cegep.foodie.list.RecipeAdapter;
import com.cegep.foodie.list.StepsAdapter;
import com.cegep.foodie.model.Ingredient;
import com.cegep.foodie.model.Recipe;
import com.cegep.foodie.ui.createrecipe.EditRecipe;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView recipeName ;
     TextView recipeTime ;
    TextView servingSize ;
    ImageView recipeImage ;
    ImageView deleterecipe ;
    ImageView updateRecipe ;
    RecyclerView stepsList ;
    ArrayList<Recipe> recipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        recipeName = (TextView)findViewById(R.id.recipe_name);
        recipeTime = (TextView)findViewById(R.id.recipe_time);
        servingSize = (TextView)findViewById(R.id.serving_size);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        deleterecipe = (ImageView) findViewById(R.id.deleterecipe);
        updateRecipe = (ImageView) findViewById(R.id.updateRecipe);
        stepsList = findViewById(R.id.preparationlist);


         Intent intent=getIntent();
         Recipe recipe=intent.getParcelableExtra("userdata");


        recipeName.setText(recipe.getName());
        servingSize.setText(String.format("Serves %d people", recipe.getServingSize()));
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        Glide.with(this)
                .load(recipe.getImage())
                .into(recipeImage);
        int duration = recipe.getDuration();
        if (duration > 60) {
            recipeTime.setText((recipe.getDuration() / 60) + " hrs");
        } else {
            recipeTime.setText(recipe.getDuration() + " mins");
       }



        deleterecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("RecipeCategory").child(recipe.getCategory()).child(recipe.getId()).removeValue();
                Intent i = new Intent(RecipeDetailActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        updateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecipeDetailActivity.this, EditRecipe.class);
                i.putExtra("recipeId",recipe.getId());
                i.putExtra("recipeCategory",recipe.getCategory());
                startActivity(i);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("RecipeCategory").child(recipe.getCategory()).child(recipe.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                 Recipe recipeData = (Recipe) snapshot.getValue(Recipe.class);

                ChipGroup chipGroup = findViewById(R.id.ingredients_group);
                for (Ingredient ingredient : recipeData.getIngredients()) {
                    Chip chip = (Chip) LayoutInflater.from(RecipeDetailActivity.this).inflate(R.layout.ingredient_chip, chipGroup, false);
                    chip.setCloseIconVisible(false);
                    chip.setText(ingredient.getName());

                    chipGroup.addView(chip);
                }

                StepsAdapter stepsAdapter = new StepsAdapter(null, false);
                stepsList.setAdapter(stepsAdapter);
                stepsAdapter.submitList(recipeData.getPreparationSteps());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
}