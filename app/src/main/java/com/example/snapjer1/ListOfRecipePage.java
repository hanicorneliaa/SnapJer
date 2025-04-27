package com.example.snapjer1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ListOfRecipePage extends AppCompatActivity {

    private TextView recipe_type;
    private TextView recipe_allergy;
    private TextView recipe_ingredients;
    private List<String> ingredients = new ArrayList<>();
    private List<String> type = new ArrayList<>();
    private List<String> choices = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mRecipeList;
    private TextView norecipe;
    private int page = 0; //save button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_recipe_page);
        recipe_type = findViewById(R.id.cuisine);
        recipe_allergy = findViewById(R.id.allergy);
        recipe_ingredients = findViewById(R.id.ingredients);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecipeList = findViewById(R.id.recipe_list);
        norecipe = findViewById(R.id.norecipefound);
        norecipe.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recipes found");


        ingredients = (List<String>) getIntent().getSerializableExtra("ingredients");
        type = (List<String>) getIntent().getSerializableExtra("cuisine");
        choices = (List<String>) getIntent().getSerializableExtra("choices");

        String choice1 = "";
        int choice2 = 0;

        if(choices.size() == 1){
            if(choices.get(0).equals("Easy")){
                choice1 = choices.get(0);
                choice2 = 0;
            }
            else{
                choice1 = "";
                choice2 = Integer.parseInt(choices.get(0));
            }
        }
        else if(choices.size() == 2){
            if(choices.get(0).equals("Easy")){
                choice1 = choices.get(0);
                choice2 = Integer.parseInt(choices.get(1));
            }
            else{
                choice1 = choices.get(1);
                choice2 = Integer.parseInt(choices.get(1));

            }
        }

        Query query;
        if(type.size() == 0 ){
            if(choice1.equals("Easy")){
                if(ingredients.size() == 1){ //1 ingredient
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeDifficulty", choice1);
                }
                else if(ingredients.size() == 2){ //2 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeDifficulty", choice1);
                }
                else{ //3 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(2), true)
                            .whereEqualTo("recipeDifficulty", choice1);
                }
            }
            else{
                if(ingredients.size() == 1){ //1 ingredient
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true);
                }
                else if(ingredients.size() == 2){ //2 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true);
                }
                else{ //3 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(2), true);
                }
            }

        }
        else if(type.size() == 1){
            if(choice1.equals("Easy")){
                if(ingredients.size() == 1){ //1 ingredient
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeType", type.get(0))
                            .whereEqualTo("recipeDifficulty", choice1);
                }
                else if(ingredients.size() == 2){ //2 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeType", type.get(0))
                            .whereEqualTo("recipeDifficulty", choice1);
                }
                else{ //3 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(2), true)
                            .whereEqualTo("recipeType", type.get(0));
                }
            }
            else{
                if(ingredients.size() == 1){ //1 ingredient
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeType", type.get(0));
                }
                else if(ingredients.size() == 2){ //2 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeType", type.get(0));
                }
                else { //3 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(2), true)
                            .whereEqualTo("recipeType", type.get(0));
                }
            }

        }
        else{
            if(choice1.equals("Easy")){
                if(ingredients.size() == 1){ //1 ingredient
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereIn("recipeType", Arrays.asList(type.get(0), type.get(1)))
                            .whereEqualTo("recipeDifficulty", choice1);
                }
                else if(ingredients.size() == 2){ //2 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereIn("recipeType", Arrays.asList(type.get(0), type.get(1)))
                            .whereEqualTo("recipeDifficulty", choice1);
                }
                else{ //3 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(2), true)
                            .whereIn("recipeType", Arrays.asList(type.get(0), type.get(1)))
                            .whereEqualTo("recipeDifficulty", choice1);
                }
            }
            else{
                if(ingredients.size() == 1){ //1 ingredient
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereIn("recipeType", Arrays.asList(type.get(0), type.get(1)));
                }
                else if(ingredients.size() == 2){ //2 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereIn("recipeType", Arrays.asList(type.get(0), type.get(1)));
                }
                else{ //3 ingredients
                    query = firebaseFirestore.collection("Recipe")
                            .whereEqualTo("recipeIngredients." + ingredients.get(0), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(1), true)
                            .whereEqualTo("recipeIngredients." + ingredients.get(2), true)
                            .whereIn("recipeType", Arrays.asList(type.get(0), type.get(1)));
                }
            }

        }


        recipe_type.setVisibility(View.GONE);
        recipe_ingredients.setVisibility(View.GONE);
        recipe_allergy.setVisibility(View.GONE);



        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(4)
                .setPageSize(20)
                .build();

        FirestorePagingOptions<Recipe> options = new FirestorePagingOptions.Builder<Recipe>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<Recipe>() {
                    @NonNull
                    @Override
                    public Recipe parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Recipe recipe = snapshot.toObject(Recipe.class);
                        String recipeId = snapshot.getId();
                        String recipeImage = (String) snapshot.get("recipeImage");
                        recipe.setRecipeID(recipeId);
                        recipe.setRecipeImage(recipeImage);
                        return recipe;
                    }
                })
                .build();

        final int finalChoice = choice2;
        FirestorePagingAdapter<Recipe, FirestoreAdapter.RecipeViewHolder> adapter =
                new FirestorePagingAdapter<Recipe, FirestoreAdapter.RecipeViewHolder>(options) {
                    @NonNull
                    @Override
                    public FirestoreAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe_single, parent, false);
                        return new FirestoreAdapter.RecipeViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull FirestoreAdapter.RecipeViewHolder holder, int position, @NonNull Recipe model) {

                        final String name = model.getRecipeName();
                        final String image = model.getRecipeImageURL();
                        final String id = model.getRecipeID();
                        final String type = model.getRecipeType();
                        final String description = model.getRecipeDescription();
                        final String difficulty = model.getRecipeDifficulty();
                        final String cookTime = model.getRecipeCookTime();
                        //Map<String, Boolean> ingredients = model.getRecipeIngredients();
                        final List<String> ingredientsWithMeasurement = model.getRecipeIngredientsWithMeasurement();
                        final List<String> steps = model.getRecipeSteps();

                        if(ingredientsWithMeasurement.size() <= finalChoice){
                            holder.view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), RecipePage.class);
                                    intent.putExtra("page", page);
                                    intent.putExtra("name", name);
                                    intent.putExtra("image", image);
                                    intent.putExtra("id", id);
                                    intent.putExtra("type", type);
                                    intent.putExtra("description", description);
                                    intent.putExtra("difficulty", difficulty);
                                    intent.putExtra("cookTime", cookTime);
                                    intent.putExtra("ingredients", (Serializable) ingredientsWithMeasurement);
                                    intent.putExtra("steps", (Serializable) steps);
                                    startActivity(intent);
                                }
                            });
                            holder.list_recipe_name.setText(model.getRecipeName());
                            holder.list_recipe_type.setText(model.getRecipeType());
                            holder.list_recipe_difficulty.setText(model.getRecipeDifficulty());
                            Glide.with(getApplicationContext()).load(image).into(holder.list_recipe_image);
                        }
                        else if(finalChoice == 0){
                            holder.view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), RecipePage.class);
                                    intent.putExtra("page", page);
                                    intent.putExtra("name", name);
                                    intent.putExtra("image", image);
                                    intent.putExtra("id", id);
                                    intent.putExtra("type", type);
                                    intent.putExtra("description", description);
                                    intent.putExtra("difficulty", difficulty);
                                    intent.putExtra("cookTime", cookTime);
                                    intent.putExtra("ingredients", (Serializable) ingredientsWithMeasurement);
                                    intent.putExtra("steps", (Serializable) steps);
                                    startActivity(intent);
                                }
                            });
                            holder.list_recipe_name.setText(model.getRecipeName());
                            holder.list_recipe_type.setText(model.getRecipeType());
                            holder.list_recipe_difficulty.setText(model.getRecipeDifficulty());
                            Glide.with(getApplicationContext()).load(image).into(holder.list_recipe_image);
                        }
                        else{
                            holder.list_recipe_name.setVisibility(View.GONE);
                            holder.list_recipe_type.setVisibility(View.GONE);
                            holder.list_recipe_difficulty.setVisibility(View.GONE);
                            holder.list_recipe_image.setVisibility(View.GONE);
                        }

                    }
                };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecipeList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        mRecipeList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecipeList.setHasFixedSize(false);
        mRecipeList.setLayoutManager(gridLayoutManager);
        mRecipeList.setAdapter(adapter);

        if (mRecipeList != null){
            norecipe.setVisibility(View.GONE);
        }
        else {
            norecipe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            case R.id.dashboard:
                goToDashboard();
                return true;
            case R.id.home:
                goToHome();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToHome() {
        startActivity(new Intent(getApplicationContext(),StartPage.class));
        finish();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void goToDashboard() {
        startActivity(new Intent(getApplicationContext(),DashboardPage.class));
        finish();
    }
}

