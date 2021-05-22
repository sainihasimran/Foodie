package com.cegep.foodie.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class HomeFragment extends Fragment {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    TextView searchTextLabel;
   Button searchButton;
   String flag="false";
    ArrayList<Recipe> recipeArrayList;

    ImageView vegetarian,meat,vegan,drinks;



    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.homeRecipe);
        searchButton=(Button) view.findViewById(R.id.searchButton);
        searchTextLabel=(TextView) view.findViewById(R.id.searchTextLabel);

        vegetarian=(ImageView) view.findViewById(R.id.vegetarian);
        meat=(ImageView) view.findViewById(R.id.meat);
        vegan=(ImageView) view.findViewById(R.id.vegan);
        drinks=(ImageView) view.findViewById(R.id.drinks);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));



        //SEARCH RECIPE
        searchTextLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),SearchActivity.class);
                startActivity(i);
            }
        });

        vegetarian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    navigateToRecipeCategory("Vegetarian");
                }
            });


        meat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    navigateToRecipeCategory("Non vegetarian");
                }
            });

        drinks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    navigateToRecipeCategory("Drinks");
                }
            });

            vegan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    navigateToRecipeCategory("Vegan");
                }
            });






        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("RecipeCategory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeArrayList=new ArrayList<>();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot recipeList : snapshot.getChildren()) {

                        Recipe recipeData = recipeList.getValue(Recipe.class);
                        recipeData.id = recipeList.getKey();

                        recipeArrayList.add(new Recipe(recipeData.id, recipeData.getName(), recipeData.getImage(), recipeData.getServingSize()
                                , recipeData.getCategory(), recipeData.getDuration(), recipeData.getCalories(), recipeData.getIngredients(), recipeData.getPreparationSteps()));

                    }
                }

                recipeAdapter= new RecipeAdapter(recipeArrayList,getActivity());
                recyclerView.setAdapter(recipeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });



        return  view;
    }

    private void navigateToRecipeCategory( String childValue) {
        Intent i = new Intent(getContext(),RecipeByCategory.class);
        i.putExtra("childValue",childValue);
        startActivity(i);
    }
}

























