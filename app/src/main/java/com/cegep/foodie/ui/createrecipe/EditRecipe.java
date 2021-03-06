package com.cegep.foodie.ui.createrecipe;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cegep.foodie.R;
import com.cegep.foodie.list.StepsAdapter;
import com.cegep.foodie.model.Category;
import com.cegep.foodie.model.Ingredient;
import com.cegep.foodie.model.PreparationStep;
import com.cegep.foodie.model.Recipe;
import com.cegep.foodie.ui.MainActivity;
import com.cegep.foodie.ui.RecipeDetailActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditRecipe extends AppCompatActivity {
    RecyclerView stepsList ;
    public StepsAdapter stepsAdapter;
    EditText updateRecipeName,updateServingSize,updateTime,updateCalories;
    AutoCompleteTextView timetype,updateCategory;
    private final List<Ingredient> ingredientSteps = new ArrayList<>();
public String imageSource;
    ChipGroup chipGroup;
    private final List<PreparationStep> preparationSteps = new ArrayList<>();
    Button updateRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Recipe");

        String recipeId = getIntent().getStringExtra("recipeId");
        String recipeCategory = getIntent().getStringExtra("recipeCategory");
         stepsList = findViewById(R.id.steps_recycler_view);

        updateRecipeName = findViewById(R.id.updateRecipeName);
        updateServingSize = findViewById(R.id.updateServingSize);
        updateCategory = findViewById(R.id.updateCategory);
        updateTime = findViewById(R.id.updateTime);
        updateCalories = findViewById(R.id.updateCalories);
        updateRecipe = findViewById(R.id.updateRecipe);
        timetype = findViewById(R.id.time_type);







        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("RecipeCategory").child(recipeCategory).child(recipeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Recipe recipeData = (Recipe) snapshot.getValue(Recipe.class);
                updateRecipeName.setText(recipeData.getName());
                updateServingSize.setText( String.valueOf(recipeData.getServingSize()));
                updateCategory.setText(recipeData.getCategory());

                int duration = recipeData.getDuration();
                if (duration / 60 > 0) {
                    timetype.setText("hrs");
                    updateTime.setText(String.valueOf(duration / 60));
                } else {
                    timetype.setText("mins");
                    updateTime.setText(String.valueOf(duration));
                }

                setupCategories();
                setupDuration();

                updateCalories.setText(String.valueOf(recipeData.getCalories()));
                imageSource=recipeData.getImage();
                 chipGroup = findViewById(R.id.ingredients_group);
                for (Ingredient ingredient : recipeData.getIngredients()) {
                    Chip chip = (Chip) LayoutInflater.from(EditRecipe.this).inflate(R.layout.ingredient_chip, chipGroup, false);
                    chip.setCloseIconVisible(false);
                    chip.setText(ingredient.getName());
                    ingredientSteps.add(ingredient);
                    chipGroup.addView(chip);
                }

           for (PreparationStep step : recipeData.getPreparationSteps()) {
              // Log.d("step", String.valueOf(step));

               preparationSteps.add(step);
                }

                 stepsAdapter = new StepsAdapter(null, false);

                stepsList.setAdapter(stepsAdapter);
                stepsAdapter.submitList(preparationSteps);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        findViewById(R.id.add_preparation_step).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.add_preparation_step);

            View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_text_input_layout, null, false);
            EditText inputEditText = view1.findViewById(R.id.input_text);
            builder.setView(view1);

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String preparationStepStr = inputEditText.getText().toString();
                PreparationStep preparationStep = new PreparationStep();
                preparationStep.stepDescription = preparationStepStr;

                preparationSteps.add(preparationStep);
                stepsAdapter.submitList(new ArrayList<>(preparationSteps));
            });

            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            dialog.show();
        });


        findViewById(R.id.add_ingredient_chip).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.add_ingredient);

            View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_text_input_layout, null, false);
            EditText inputEditText = view1.findViewById(R.id.input_text);
            builder.setView(view1);

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String ingredientStr = inputEditText.getText().toString();
                Chip chip = (Chip) LayoutInflater.from(this).inflate(R.layout.ingredient_chip, chipGroup, false);
                chip.setText(ingredientStr);
                chip.setOnCloseIconClickListener(v1 -> {
                    chipGroup.removeView(chip);
                    ingredientSteps.remove(String.valueOf(chip.getText()));
                });

                int numOfChildren = chipGroup.getChildCount();

                chipGroup.addView(chip, numOfChildren - 1);

                Ingredient ingredient = new Ingredient();
                ingredient.name = ingredientStr;
                ingredientSteps.add(ingredient);
            });

            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            dialog.show();
        });



        updateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int calories = Integer.parseInt(((EditText) findViewById(R.id.updateCalories)).getText().toString());
                int servingSize = Integer.parseInt(((EditText) findViewById(R.id.updateServingSize)).getText().toString());

                int time = Integer.parseInt(updateTime.getText().toString());
                time = timetype.getText().toString().equals("hrs") ? time * 60 : time;

                HashMap<String, Object> updateRecipe = new HashMap<>();
                updateRecipe.put("name", updateRecipeName.getText().toString());
                updateRecipe.put("calories", calories);
                updateRecipe.put("image", imageSource);
                updateRecipe.put("servingSize", servingSize);
                updateRecipe.put("category", updateCategory.getText().toString());
                updateRecipe.put("duration", time);
                updateRecipe.put("ingredients", ingredientSteps);
                updateRecipe.put("preparationSteps", preparationSteps);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                if (!recipeCategory.equals(updateCategory.getText().toString())) {
                    databaseReference.child("RecipeCategory").child(recipeCategory).child(recipeId).removeValue();
                }
                databaseReference.child("RecipeCategory").child(updateCategory.getText().toString()).child(recipeId).setValue(updateRecipe);

                Intent i = new Intent(EditRecipe.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });


    }

    private void setupCategories() {
        final List<String> categories = new ArrayList<>(Category.values().length);
        for (Category category : Category.values()) {
            categories.add(category.getNiceName());
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        AutoCompleteTextView recipeCategoryTextView = findViewById(R.id.updateCategory);
        recipeCategoryTextView.setAdapter(categoryAdapter);

        recipeCategoryTextView.setOnItemClickListener((parent, view1, position, id) -> updateCategory.setText(categories.get(position)));
    }

    private void setupDuration() {
        final List<String> durations = new ArrayList<>();
        durations.add("hrs");
        durations.add("mins");

        ArrayAdapter<String> durationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, durations);
        AutoCompleteTextView durationsTextView = findViewById(R.id.time_type);
        durationsTextView.setAdapter(durationsAdapter);

        durationsTextView.setOnItemClickListener((parent, view1, position, id) -> timetype.setText(durations.get(position)));
    }
}


