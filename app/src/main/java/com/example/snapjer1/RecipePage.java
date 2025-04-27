package com.example.snapjer1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.List;

public class RecipePage extends AppCompatActivity {

    ImageView recipe_image;
    TextView recipe_name;
    TextView recipe_description;
    TextView recipe_type;
    TextView recipe_ingredients;
    TextView recipe_steps;
    TextView recipe_difficulty;
    TextView recipe_cookTime;
    Button saveButton, removeButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    Button overviewBtn;
    Button ingredientsBtn;
    Button stepsBtn;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);
        recipe_name = findViewById(R.id.recipe_name);
        recipe_image = findViewById(R.id.recipe_image);
        recipe_description = findViewById(R.id.recipe_description);
        recipe_type = findViewById(R.id.recipe_type);
        recipe_ingredients = findViewById(R.id.recipe_ingredients);
        recipe_steps = findViewById(R.id.recipe_steps);
        recipe_difficulty = findViewById(R.id.recipe_difficulty);
        recipe_cookTime = findViewById(R.id.recipe_cookTime);
        saveButton = findViewById(R.id.saveButton);
        removeButton = findViewById(R.id.removeButton);
        overviewBtn = findViewById(R.id.overviewBtn);
        ingredientsBtn = findViewById(R.id.ingredientsBtn);
        stepsBtn = findViewById(R.id.stepsBtn);

        overviewBtn.setTextColor(Color.parseColor("#A80D1A"));

        page = getIntent().getIntExtra("page", 0);
        final String name = getIntent().getStringExtra("name");
        final String id = getIntent().getStringExtra("id");
        final String image = getIntent().getStringExtra("image");
        final String description = getIntent().getStringExtra("description");
        final String type = getIntent().getStringExtra("type");
        final String difficulty = getIntent().getStringExtra("difficulty");
        final String cookTime = getIntent().getStringExtra("cookTime");
        final List<String> ingredients = (List<String>) getIntent().getSerializableExtra("ingredients");
        final List<String> steps = (List<String>) getIntent().getSerializableExtra("steps");


        recipe_name.setText(name);
        Glide.with(getApplicationContext()).load(image).into(recipe_image);
        recipe_description.setText(description);
        recipe_type.setText(type);
        recipe_difficulty.setText(difficulty);
        recipe_cookTime.setText(cookTime);
        recipe_ingredients.setVisibility(View.GONE);
        recipe_steps.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Recipe recipe = new Recipe(id, name, type, description, image, ingredients, steps, difficulty, cookTime);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth = FirebaseAuth.getInstance();
                fstore = FirebaseFirestore.getInstance();
                userID = fAuth.getCurrentUser().getUid();

                //check if the recipe already saved in the dashboard
                CollectionReference allRecipeID = fstore.collection("UserRecipe").document(userID).collection("RecipeSaved");
                Query query = allRecipeID.whereEqualTo("recipeID", id);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Boolean check = true;
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Toast.makeText(RecipePage.this, "Recipe already saved in your dashboard!", Toast.LENGTH_SHORT).show();
                                    check = false;
                                }
                            }
                            if(check){
                                fstore.collection("UserRecipe").document(userID).collection("RecipeSaved").add(recipe)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RecipePage.this, "Recipe Saved!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth = FirebaseAuth.getInstance();
                fstore = FirebaseFirestore.getInstance();
                userID = fAuth.getCurrentUser().getUid();
                fstore.collection("UserRecipe").document(userID).collection("RecipeSaved").document(id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RecipePage.this, "Recipe Removed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        if(page == 0){
            removeButton.setVisibility(View.GONE);
        }
        else{
            saveButton.setVisibility(View.GONE);
        }

        overviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipePage.class);
                intent.putExtra("name", name);
                intent.putExtra("image", image);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("description", description);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("cookTime", cookTime);
                intent.putExtra("ingredients", (Serializable) ingredients);
                intent.putExtra("steps", (Serializable) steps);
                startActivity(intent);

            }
        });

        ingredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipePageIngredients.class);
                intent.putExtra("name", name);
                intent.putExtra("image", image);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("description", description);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("cookTime", cookTime);
                intent.putExtra("ingredients", (Serializable) ingredients);
                intent.putExtra("steps", (Serializable) steps);
                startActivity(intent);

            }
        });

        stepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipePageSteps.class);
                intent.putExtra("name", name);
                intent.putExtra("image", image);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("description", description);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("cookTime", cookTime);
                intent.putExtra("ingredients", (Serializable) ingredients);
                intent.putExtra("steps", (Serializable) steps);
                startActivity(intent);

            }
        });





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
                if(page==0){
                    finish();
                    return true;
                }
                else{
                    startActivity(new Intent(getApplicationContext(), DashboardPage.class));
                }
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
