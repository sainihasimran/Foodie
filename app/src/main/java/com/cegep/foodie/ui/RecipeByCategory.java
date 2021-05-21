package com.cegep.foodie.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cegep.foodie.R;
import com.cegep.foodie.list.RecipeAdapter;
import com.cegep.foodie.model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeByCategory extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    ArrayList<Recipe> recipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_by_category);

        Bundle extras = getIntent().getExtras();

            String childValue = extras.getString("childValue");


        recyclerView=(RecyclerView)findViewById(R.id.recipeByCategory);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(RecipeByCategory.this, 3));


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("RecipeCategory").child(childValue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recipeArrayList=new ArrayList<>();




                    for (DataSnapshot recipeList : dataSnapshot.getChildren()) {

                        Recipe recipeData = recipeList.getValue(Recipe.class);
                        recipeData.id = recipeList.getKey();


                        recipeArrayList.add(new Recipe(recipeData.id, recipeData.getName(), recipeData.getImage(), recipeData.getServingSize()
                                , recipeData.getCategory(), recipeData.getDuration(), recipeData.getCalories(), recipeData.getIngredients(), recipeData.getPreparationSteps()));


                    }


                recipeAdapter= new RecipeAdapter(recipeArrayList,RecipeByCategory.this);
                recyclerView.setAdapter(recipeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecipeByCategory.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });

    }
}