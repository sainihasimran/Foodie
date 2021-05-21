package com.cegep.foodie.list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cegep.foodie.R;
import com.cegep.foodie.model.Ingredient;
import com.cegep.foodie.model.Recipe;
import com.cegep.foodie.ui.RecipeDetailActivity;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    ArrayList<Recipe> recipeArrayList;
    Context context;

    public RecipeAdapter(ArrayList<Recipe> recipeArrayList, Context context) {
        this.recipeArrayList = recipeArrayList;
        this.context = context;
    }



    public  static class MyViewHolder extends RecyclerView.ViewHolder
    {


        ImageView recipe_image;
        TextView recipe_name_text;
        View recipeRecycler;


        MyViewHolder (View itemview)
        {
            super(itemview);

            this.recipe_image=(ImageView)itemview.findViewById(R.id.recipe_image);
            this.recipe_name_text=(TextView)itemview.findViewById(R.id.recipe_name_text);
            this.recipeRecycler=(View) itemview.findViewById(R.id.recipeRecycler);


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li= LayoutInflater.from(parent.getContext());
        View view=li.inflate(R.layout.item_recipe,parent,false);
        MyViewHolder myViewHolder= new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        ImageView recipe_image=holder.recipe_image;
        TextView recipe_name_text=holder.recipe_name_text;
        View recipeRecycler=holder.recipeRecycler;


     //   id.setText(userArrayList.get(position).getId());
        recipe_name_text.setText(recipeArrayList.get(position).getName());

        Glide.with(context)
                .asBitmap()
                .load(recipeArrayList.get(position).getImage())
                .into(recipe_image);



        recipeRecycler.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(context, RecipeDetailActivity.class);

                intent.putExtra("userdata",  recipeArrayList.get(position));
                context.startActivity(intent);
//

            }
        });



    }


    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }
}
