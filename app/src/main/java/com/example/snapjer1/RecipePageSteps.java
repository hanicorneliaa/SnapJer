package com.example.snapjer1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.List;

public class RecipePageSteps extends AppCompatActivity {

    ImageView recipe_image;
    TextView recipe_name;
    TextView recipe_description;
    TextView recipe_type;
    TextView recipe_ingredients;
    TextView recipe_steps;
    TextView recipe_difficulty;
    TextView recipe_cookTime;
    Button overviewBtn;
    Button ingredientsBtn;
    Button stepsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page_steps);
        recipe_name = findViewById(R.id.recipe_name);
        recipe_image = findViewById(R.id.recipe_image);
        recipe_description = findViewById(R.id.recipe_description);
        recipe_type = findViewById(R.id.recipe_type);
        recipe_ingredients = findViewById(R.id.recipe_ingredients);
        recipe_steps = findViewById(R.id.recipe_steps);
        recipe_difficulty = findViewById(R.id.recipe_difficulty);
        recipe_cookTime = findViewById(R.id.recipe_cookTime);
        overviewBtn = findViewById(R.id.overviewBtn);
        ingredientsBtn = findViewById(R.id.ingredientsBtn);
        stepsBtn = findViewById(R.id.stepsBtn);

        stepsBtn.setTextColor(Color.parseColor("#A80D1A"));

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

        recipe_steps.setText("");
        for (int j = 0; j < steps.size(); j++){
            recipe_steps.append("Step " + (j+1) + ":\n" + steps.get(j) + "\n" + "\n");
        }

        recipe_image.setVisibility(View.GONE);
        recipe_description.setVisibility(View.GONE);
        recipe_type.setVisibility(View.GONE);
        recipe_ingredients.setVisibility(View.GONE);
        recipe_difficulty.setVisibility(View.GONE);
        recipe_cookTime.setVisibility(View.GONE);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
