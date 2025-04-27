package com.example.snapjer1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;


public class FirestoreAdapter extends FirestorePagingAdapter<Recipe, FirestoreAdapter.RecipeViewHolder> {

    private OnListItemClick onListItemClick;
    private String recipe_name;

    public FirestoreAdapter(@NonNull FirestorePagingOptions<Recipe> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe model) {
        holder.list_recipe_name.setText(model.getRecipeName());
        holder.list_recipe_type.setText(model.getRecipeType());
        holder.list_recipe_difficulty.setText(model.getRecipeDifficulty());

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe_single, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch(state){
            case LOADING_INITIAL:
                Log.d("PAGING_LOG", "Loading Initial Data");
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG", "Loading Next Page");
                break;
            case FINISHED:
                Log.d("PAGING_LOG", "All Data Loaded");
                break;
            case ERROR:
                Log.d("PAGING_LOG", "Error Loading Data");
                break;
            case LOADED:
                Log.d("PAGING_LOG", "Total Items Loaded: " + getItemCount());
                break;
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView list_recipe_name;
        public TextView list_recipe_type;
        public TextView list_recipe_description;
        public ImageView list_recipe_image;
        public TextView list_recipe_ingredients;
        public TextView list_recipe_ingredients_with_measurements;
        public TextView list_recipe_steps;
        public TextView list_recipe_difficulty;
        public TextView list_recipe_cook_time;
        View view;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            list_recipe_name = itemView.findViewById(R.id.list_recipe_name);
            list_recipe_type = itemView.findViewById(R.id.list_recipe_type);
            list_recipe_description = itemView.findViewById(R.id.list_recipe_description);
            list_recipe_image = itemView.findViewById(R.id.list_recipe_image);
            list_recipe_ingredients = itemView.findViewById(R.id.list_recipe_ingredients);
            list_recipe_ingredients_with_measurements = itemView.findViewById(R.id.list_recipe_ingredients_with_measurements);
            list_recipe_steps = itemView.findViewById(R.id.list_recipe_steps);
            list_recipe_difficulty = itemView.findViewById(R.id.list_recipe_difficulty);
            list_recipe_cook_time = itemView.findViewById(R.id.list_recipe_cook_time);

            view = itemView;

            list_recipe_description.setVisibility(View.GONE);
            list_recipe_ingredients.setVisibility(View.GONE);
            list_recipe_ingredients_with_measurements.setVisibility(View.GONE);
            list_recipe_steps.setVisibility(View.GONE);
            list_recipe_cook_time.setVisibility(View.GONE);

        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
