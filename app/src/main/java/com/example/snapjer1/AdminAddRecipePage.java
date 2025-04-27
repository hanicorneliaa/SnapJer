package com.example.snapjer1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAddRecipePage extends AppCompatActivity {

    EditText mRecipeName,mRecipeDescription,mRecipeIngredients,mRecipeIngredientsWithMeasurement,mRecipeSteps,mRecipeCookTime;
    Button mAddRecipeBtn;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton mRecipeType, mRecipeDifficulty;
    ImageView imageRecipe;
    Map<String, Boolean> ingredient = new HashMap<>();
    StorageReference reference;
    private Uri imageUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_recipe_page);

        mRecipeName = findViewById(R.id.recipe_name);
        radioGroup1 = findViewById(R.id.radioGroup);
        int radioId1 = radioGroup1.getCheckedRadioButtonId();
        mRecipeType = findViewById(radioId1);
        mRecipeDescription = findViewById(R.id.recipe_description);
        mRecipeIngredients = findViewById(R.id.recipe_ingredients);
        mRecipeIngredientsWithMeasurement = findViewById(R.id.recipe_ingredients_with_measurement);
        mRecipeSteps = findViewById(R.id.recipe_steps);
        radioGroup2 = findViewById(R.id.radioGroup2);
        int radioId2 = radioGroup2.getCheckedRadioButtonId();
        mRecipeDifficulty = findViewById(radioId2);
        mRecipeCookTime = findViewById(R.id.recipe_cookTime);
        mAddRecipeBtn = findViewById(R.id.addRecipeBtn);
        imageRecipe = findViewById(R.id.imageRecipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(AdminAddRecipePage.this);
        reference = FirebaseStorage.getInstance().getReference();

        imageRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        mAddRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mRecipeName.getText().toString();
                String type = mRecipeType.getText().toString();
                String description = mRecipeDescription.getText().toString();
                String difficulty = mRecipeDifficulty.getText().toString();
                String cookTime = mRecipeCookTime.getText().toString();

                String ingredients = mRecipeIngredients.getText().toString();
                String[] ingredientArray = ingredients.split("\n");
                for(String ingred : ingredientArray){
                    ingredient.put(ingred, true);
                }

                String ingredientsWithMeasurement = mRecipeIngredientsWithMeasurement.getText().toString();
                String[] ingredientsArray = ingredientsWithMeasurement.split("\n");
                List<String> ingredientWithMeasurement = Arrays.asList(ingredientsArray);

                String steps = mRecipeSteps.getText().toString();
                String[] stepArray = steps.split("\n");
                List<String> step = Arrays.asList(stepArray);

                if(TextUtils.isEmpty(name)){
                    mRecipeName.setError("Recipe name is required.");
                    return;
                }

                if(TextUtils.isEmpty(description)){
                    mRecipeDescription.setError("Recipe description is required.");
                    return;
                }

                if(TextUtils.isEmpty(ingredients)){
                    mRecipeIngredients.setError("Recipe ingredients is required.");
                    return;
                }

                if(TextUtils.isEmpty(ingredientsWithMeasurement)){
                    mRecipeIngredientsWithMeasurement.setError("Recipe ingredients with measurement is required.");
                    return;
                }

                if(TextUtils.isEmpty(steps)){
                    mRecipeSteps.setError("Recipe steps is required.");
                    return;
                }

                if(TextUtils.isEmpty(cookTime)){
                    mRecipeCookTime.setError("Recipe cook time is required.");
                    return;
                }

                if(imageUri == null){
                    Toast.makeText(AdminAddRecipePage.this, "Please select an image.", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadToFirestore(imageUri, name, type, description, ingredient, ingredientWithMeasurement, step, difficulty, cookTime);


            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageRecipe.setImageURI(imageUri);
        }
    }

    private void uploadToFirestore(Uri uri, final String name, final String type, final String description, final Map<String, Boolean> ingredient,
                                  final List<String> ingredientWithMeasurement, final List<String> step, final String difficulty, final String cookTime){

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        final FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        progressDialog.setTitle("Recipe is Uploading...");
        progressDialog.show();

        UploadTask uploadTask = fileRef.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String url = downloadUri.toString();

                    Map<String,Object> recipe = new HashMap<>();
                    recipe.put("recipeName", name);
                    recipe.put("recipeType", type);
                    recipe.put("recipeDescription", description);
                    recipe.put("recipeIngredients", ingredient);
                    recipe.put("recipeIngredientsWithMeasurement", ingredientWithMeasurement);
                    recipe.put("recipeSteps", step);
                    recipe.put("recipeDifficulty", difficulty);
                    recipe.put("recipeCookTime", cookTime);
                    recipe.put("recipeImage", url);

                    fstore.collection("Recipe").add(recipe)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminAddRecipePage.this, "Recipe added successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), AdminAddRecipePage.class));
                                    }
                                    else{
                                        Toast.makeText(AdminAddRecipePage.this, "Recipe cannot be added!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(AdminAddRecipePage.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),AdminLogin.class));
        finish();
    }

    public void cuisineButton(View view) {
        int radioId1 = radioGroup1.getCheckedRadioButtonId();
        mRecipeType = findViewById(radioId1);

        int radioId2 = radioGroup2.getCheckedRadioButtonId();
        mRecipeDifficulty = findViewById(radioId2);

    }
}
