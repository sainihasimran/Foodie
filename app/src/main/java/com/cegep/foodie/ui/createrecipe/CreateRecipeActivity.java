package com.cegep.foodie.ui.createrecipe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.R;
import com.cegep.foodie.list.StepsAdapter;
import com.cegep.foodie.list.callback.RemoveItemClickListener;
import com.cegep.foodie.model.Category;
import com.cegep.foodie.model.Ingredient;
import com.cegep.foodie.model.PreparationStep;
import com.cegep.foodie.model.Recipe;
import com.cegep.foodie.model.RecipeCategory;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRecipeActivity extends AppCompatActivity implements RemoveItemClickListener {

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int PERMISSION_REQUEST_CODE = 123;

    private static final int CHOOSE_IMAGE_REQUEST_CODE = 485;

    private Chip addImageChip;

    private ImageView recipeImageView;

    private StepsAdapter stepsAdapter;

    private final List<Ingredient> ingredients = new ArrayList<>();

    private final List<PreparationStep> preparationSteps = new ArrayList<>();

    private String selectedCategory;

    private String selectedDuration;

    private String selectedImage;

    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        addImageChip = findViewById(R.id.add_image);
        recipeImageView = findViewById(R.id.recipe_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Create Recipe");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.add_image).setOnClickListener(v -> {
            String permission = REQUIRED_PERMISSIONS[0];
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
            }
        });
        setupCategories();
        setupDuration();
        setupAddIngredientsChip();
        setupStepsList();

        findViewById(R.id.create_recipe_button).setOnClickListener(v -> {
            boolean formValid = validateForm();
            if (formValid) {
                if (!TextUtils.isEmpty(selectedImage)) {
                    uploadPhoto();
                } else {
                    createRecipe();

                }
            }

        });
    }

    private void setupCategories() {
        final List<String> categories = new ArrayList<>(Category.values().length);
        for (Category category : Category.values()) {
            categories.add(category.getNiceName());
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        AutoCompleteTextView recipeCategoryTextView = findViewById(R.id.recipe_category);
        recipeCategoryTextView.setAdapter(categoryAdapter);

        recipeCategoryTextView.setOnItemClickListener((parent, view1, position, id) -> selectedCategory = categories.get(position));
    }

    private void setupDuration() {
        final List<String> durations = new ArrayList<>();
        durations.add("hrs");
        durations.add("mins");

        ArrayAdapter<String> durationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, durations);
        AutoCompleteTextView durationsTextView = findViewById(R.id.time_type);
        durationsTextView.setAdapter(durationsAdapter);

        durationsTextView.setOnItemClickListener((parent, view1, position, id) -> selectedDuration = durations.get(position));
    }

    private void setupAddIngredientsChip() {
        ChipGroup chipGroup = findViewById(R.id.ingredients_group);
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
                    ingredients.remove(String.valueOf(chip.getText()));
                });

                int numOfChildren = chipGroup.getChildCount();

                chipGroup.addView(chip, numOfChildren - 1);

                Ingredient ingredient = new Ingredient();
                ingredient.name = ingredientStr;
                ingredients.add(ingredient);
            });

            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            dialog.show();
        });
    }

    private void setupStepsList() {
        RecyclerView stepsList = findViewById(R.id.steps_recycler_view);
        stepsAdapter = new StepsAdapter(this, false);
        stepsList.setAdapter(stepsAdapter);

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(this, R.string.storage_permission_declined, Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .maxSelectable(1)
                .forResult(CHOOSE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_IMAGE_REQUEST_CODE && data != null) {
            List<String> selectionData = Matisse.obtainPathResult(data);
            if (selectionData == null || selectionData.isEmpty()) {
                Toast.makeText(this, R.string.choose_image_error, Toast.LENGTH_SHORT).show();
            } else {
                selectedImage = selectionData.get(0);
                recipeImageView.setImageURI(Matisse.obtainResult(data).get(0));
                addImageChip.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, R.string.choose_image_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveClick(PreparationStep step, int position) {
        preparationSteps.remove(step);
        stepsAdapter.submitList(new ArrayList<>(preparationSteps));
    }

    private boolean validateForm() {
        recipe = new Recipe();

        String name = ((EditText) findViewById(R.id.recipe_name_editText)).getText().toString();
        if (TextUtils.isEmpty(name)) {
            showErrorDialog(R.string.error_empty_recipe_name);
            return false;
        }
        recipe.setName(name);

        try {
            int servingSize = Integer.parseInt(((EditText) findViewById(R.id.serving_size_editText)).getText().toString());
            recipe.setServingSize(servingSize);
        } catch (Exception e) {
            showErrorDialog(R.string.error_empty_recipe_serving_size);
            return false;
        }

        if (TextUtils.isEmpty(selectedCategory)) {
            showErrorDialog(R.string.error_empty_recipe_category);
            return false;
        }
        recipe.setCategory(selectedCategory);

        try {
            int time = Integer.parseInt(((EditText) findViewById(R.id.recipe_time_editText)).getText().toString());
            if (TextUtils.isEmpty(selectedDuration)) {
                showErrorDialog(R.string.error_empty_recipe_duration);
                return false;
            }

            recipe.setDuration(selectedDuration.equals("hrs") ? time * 60 : time);
        } catch (Exception e) {
            showErrorDialog(R.string.error_empty_recipe_time);
            return false;
        }

        try {
            int calories = Integer.parseInt(((EditText) findViewById(R.id.calorie_editText)).getText().toString());
            recipe.setCalories(calories);
        } catch (Exception e) {
            showErrorDialog(R.string.error_empty_recipe_calories);
            return false;
        }

        if (ingredients.isEmpty()) {
            showErrorDialog(R.string.error_empty_recipe_ingredients);
            return false;
        }

        if (preparationSteps.isEmpty()) {
            showErrorDialog(R.string.error_empty_recipe_preparation_steps);
            return false;
        }

        return true;
    }

    private void showErrorDialog(@StringRes int msgRes) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(msgRes)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }


    private void uploadPhoto() {
        String selectedImageName = selectedImage.substring(selectedImage.lastIndexOf("/") + 1);
        try {
            InputStream stream = new FileInputStream(new File(selectedImage));
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference imageStorageReference = storageReference.child(selectedImageName);

            UploadTask uploadTask = imageStorageReference.putStream(stream);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageStorageReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    recipe.setImage(task.getResult().toString());
                    createRecipe();
                } else {
                    task.getException().printStackTrace();
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create product", Toast.LENGTH_SHORT).show();
        }
    }

    private void createRecipe() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // VEGETARIAN, NON_VEGETARIAN, VEGAN, DRINKS;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        Map<String, Object> recipeValues = new HashMap<>();
        recipeValues.put("name", recipe.getName());
        recipeValues.put("servingSize", recipe.getServingSize());
        recipeValues.put("category", recipe.getCategory());
        recipeValues.put("duration", recipe.getDuration());
        recipeValues.put("calories", recipe.getCalories());
        recipeValues.put("image", recipe.getImage());


      // CREATE RECIPE
        DatabaseReference db_ref = database.child("RecipeCategory").child(recipe.getCategory()).push(); // new key is created
        String getRecipeId = db_ref.getKey();
        db_ref.setValue( recipeValues);

        // CREATE INGREDIENT
        DatabaseReference ingredient = database.child("RecipeCategory").child(recipe.getCategory()).child(getRecipeId).child("ingredients");
        ingredient.setValue(ingredients);

        //CREATE PREPERATIONS
        DatabaseReference preparationStep = database.child("RecipeCategory").child(recipe.getCategory()).child(getRecipeId).child("preparationSteps");
        preparationStep.setValue(preparationSteps);



    }

    // TODO: 15/05/21 remove this method
//    private void generateTestRecipe() {
//        recipe = new Recipe();
//        recipe.setName("Test Recipe 1");
//        recipe.setServingSize(2);
//        recipe.setCategory("Vegan");
//        recipe.setDuration(60);
//        recipe.setCalories(1200);
//
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Ingredient 1"));
//        ingredients.add((new Ingredient("Ingredient 2")));
//        recipe.setIngredients(ingredients);
//
//        List<PreparationStep> preparationSteps = new ArrayList<>();
//        preparationSteps.add(new PreparationStep(1, "Preparation Step 1"));
//        preparationSteps.add(new PreparationStep(2, "Preparation Step 2"));
//        recipe.setPreparationSteps(preparationSteps);
//    }
}
