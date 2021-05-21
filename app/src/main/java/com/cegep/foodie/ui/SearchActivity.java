package com.cegep.foodie.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cegep.foodie.R;
import com.cegep.foodie.list.RecipeAdapter;
import com.cegep.foodie.model.Ingredient;
import com.cegep.foodie.model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    EditText searchText;
    Button searchButton;
    String flag="false";
    ArrayList<Recipe> recipeArrayList;
    ArrayList<Recipe> searchRecipeArrayList;
    ArrayList<Ingredient> ingredientArrayList;
    private Recipe setRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView=(RecyclerView)findViewById(R.id.serachRecipe);
        searchButton=(Button) findViewById(R.id.searchButton);
        searchText=(EditText) findViewById(R.id.searchText);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));

loadRecipes();

        //SEARCH RECIPE
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchvalue=searchText.getText().toString();
                flag="false";
                if(!TextUtils.isEmpty(searchvalue)) {
                    searchRecipeArrayList=new ArrayList<>();
                    for(int i=0;i<recipeArrayList.size();i++) {
                        if (recipeArrayList.get(i).getName().toLowerCase().contains(searchvalue.trim().toLowerCase())) {

                            flag="true";

                            searchRecipeArrayList.add(new Recipe(recipeArrayList.get(i).getId(),recipeArrayList.get(i).getName(),recipeArrayList.get(i).getImage()
                                    ,recipeArrayList.get(i).getServingSize(),recipeArrayList.get(i).getCategory(),recipeArrayList.get(i).getDuration(),recipeArrayList.get(i).getCalories(),
                                    recipeArrayList.get(i).getIngredients(),recipeArrayList.get(i).getPreparationSteps()));
                            recipeAdapter = new RecipeAdapter(searchRecipeArrayList, SearchActivity.this);
                            recyclerView.setAdapter(recipeAdapter);


                        }

                    }

                    if(flag.equals("false")){
                        searchRecipeArrayList.clear();
                        recipeAdapter = new RecipeAdapter(searchRecipeArrayList, SearchActivity.this);
                        recyclerView.setAdapter(recipeAdapter);

                    }
                }
                else{
                    loadRecipes();
                }
            }
        });


    }

    private void loadRecipes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("RecipeCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                recipeArrayList=new ArrayList<>();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    // ingredientArrayList=new ArrayList<>();
                    for (DataSnapshot recipeList : snapshot.getChildren()) {

                        Recipe recipeData = recipeList.getValue(Recipe.class);
                        recipeData.id = recipeList.getKey();

                        // recipeData.setIngredients(ingredientArrayList);
                        recipeArrayList.add(new Recipe(recipeData.id, recipeData.getName(), recipeData.getImage(), recipeData.getServingSize()
                                , recipeData.getCategory(), recipeData.getDuration(), recipeData.getCalories(), recipeData.getIngredients(), recipeData.getPreparationSteps()));


                    }
                }

                recipeAdapter= new RecipeAdapter(recipeArrayList,SearchActivity.this);
                recyclerView.setAdapter(recipeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }
}