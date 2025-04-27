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
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity implements FirestoreAdapter.OnListItemClick, Serializable{

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mRecipeList;
    private int page = 0; //save button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecipeList = findViewById(R.id.recipe_list);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int type = getIntent().getIntExtra("cuisine", 0);
        Query query;

        if(type == 0){
            query = firebaseFirestore.collection("Recipe");
        }
        else if(type == 1){
            query = firebaseFirestore.collection("Recipe")
                    .whereEqualTo("recipeType", "Western");
        }
        else if(type == 2){
            query = firebaseFirestore.collection("Recipe")
                    .whereEqualTo("recipeType", "Malay");
        }
        else if(type == 3){
            query = firebaseFirestore.collection("Recipe")
                    .whereEqualTo("recipeType", "Vegetarian");
        }
        else if(type == 4){
            query = firebaseFirestore.collection("Recipe")
                    .whereEqualTo("recipeType", "Chinese");
        }
        else if(type == 5){
            query = firebaseFirestore.collection("Recipe")
                    .whereEqualTo("recipeType", "Indian");
        }
        else{
            query = firebaseFirestore.collection("Recipe")
                    .whereEqualTo("recipeType", "Vegan");
        }


        PagedList.Config config = new PagedList.Config.Builder()
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
                };


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecipeList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecipeList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecipeList.setHasFixedSize(false);
        mRecipeList.setLayoutManager(gridLayoutManager);
        mRecipeList.setAdapter(adapter);

    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Log.d("ITEM_CLICK", "Clicked an Item: " + position + "and the ID: " + snapshot.getId());
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

