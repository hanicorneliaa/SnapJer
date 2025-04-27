package com.example.snapjer1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class MainActivity extends AppCompatActivity{

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 10001;
    private ImageView imageView;
    private TextView resultView;
    private Classifier imageClassifier;
    private Button takepicture, search;
    private Button deleteButton1, deleteButton2, deleteButton3;
    private List<String> cuisine = new ArrayList<String>();
    private List<String> choices = new ArrayList<String>();
    private ToggleButton western, malay, vegetarian, indian, chinese, vegan;
    private CheckBox choice1, choice2;
    private List<String> results = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.bt_search);

        choices.clear();

        InitializeUIElements();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choice1.isChecked()){
                    choices.add("Easy");
                }
                else{
                    choices.remove("Easy");
                }
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choice2.isChecked()){
                    choices.add("10");
                }
                else{
                    choices.remove("10");
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuisine.clear();
                cuisine = checkCuisine(western, malay, vegetarian, indian, chinese, vegan);

                if(results.size() > 0){
                    if(checkDuplicates(results) == false){
                        if(cuisine.size() < 3){
                            Intent intent = new Intent(getApplicationContext(), ListOfRecipePage.class);
                            intent.putExtra("ingredients", (Serializable) results);
                            intent.putExtra("cuisine", (Serializable) cuisine);
                            intent.putExtra("choices", (Serializable) choices);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please choose up to 2 cuisines only.", Toast.LENGTH_SHORT).show();
                            cuisine.clear();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "There are duplicate ingredients.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "No ingredient found! Please snap at least one ingredient.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.remove(0);
                showIngredients(results);
                if(results.size() == 1){
                    deleteButton2.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);
                }
                else if(results.size() == 2){
                    deleteButton3.setVisibility(View.GONE);
                }
                else{
                    deleteButton1.setVisibility(View.GONE);
                    deleteButton2.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);
                }
            }
        });
        deleteButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ingredient deleted.", Toast.LENGTH_SHORT).show();
                results.remove(1);
                showIngredients(results);
                if(results.size() == 1){
                    deleteButton2.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);
                }
                else if(results.size() == 2){
                    deleteButton3.setVisibility(View.GONE);
                }
                else{
                    deleteButton1.setVisibility(View.GONE);
                    deleteButton2.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);
                }
            }
        });
        deleteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ingredient deleted.", Toast.LENGTH_SHORT).show();
                results.remove(2);
                showIngredients(results);
                if(results.size() == 1){
                    deleteButton2.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);
                }
                else if(results.size() == 2){
                    deleteButton3.setVisibility(View.GONE);
                }
                else{
                    deleteButton1.setVisibility(View.GONE);
                    deleteButton2.setVisibility(View.GONE);
                    deleteButton3.setVisibility(View.GONE);
                }
            }
        });
    }

    private Boolean checkDuplicates(List<String> results) {
        Set inputSet = new HashSet(results);
        if (inputSet.size() < results.size()) {
            return true;
        }
        return false;
    }


    private List<String> checkCuisine(ToggleButton western, ToggleButton malay, ToggleButton vegetarian, ToggleButton indian, ToggleButton chinese, ToggleButton vegan) {

        if(western.isChecked()){
            cuisine.add("Western");
        }
        if(malay.isChecked()){
            cuisine.add("Malay");
        }
        if(vegetarian.isChecked()){
            cuisine.add("Vegetarian");
        }
        if(indian.isChecked()){
            cuisine.add("Indian");
        }
        if(chinese.isChecked()){
            cuisine.add("Chinese");
        }
        if(vegan.isChecked()){
            cuisine.add("Vegan");
        }

        return cuisine;

    }


    private void InitializeUIElements() {
        imageView = findViewById(R.id.imageCaptured);
        resultView = findViewById(R.id.result);
        takepicture = findViewById(R.id.bt_take_picture);
        western = findViewById(R.id.toggleButton1);
        malay = findViewById(R.id.toggleButton2);
        vegetarian = findViewById(R.id.toggleButton3);
        indian = findViewById(R.id.toggleButton4);
        chinese = findViewById(R.id.toggleButton5);
        vegan = findViewById(R.id.toggleButton6);
        deleteButton1 = findViewById(R.id.deletebutton1);
        deleteButton2 = findViewById(R.id.deletebutton2);
        deleteButton3 = findViewById(R.id.deletebutton3);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);

        deleteButton1.setVisibility(View.GONE);
        deleteButton2.setVisibility(View.GONE);
        deleteButton3.setVisibility(View.GONE);


        try {
            imageClassifier = new Classifier(this);
        } catch (IOException e) {
            Log.e("Image Classifier Error", "ERROR: " + e);
        }

        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission()){
                    openCamera();
                }
                else{
                    requestPermission();
                }
            }
        });
    }

    private boolean hasAllPermissions(int[] grantResults) {
        for(int result: grantResults){
            if(result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            if(hasAllPermissions(grantResults)){
                openCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    private void openCamera() {
        if(results.size() > 2){
            Toast.makeText(MainActivity.this, "You already have 3 ingredients ",  Toast.LENGTH_SHORT).show();
        }
        else{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.GONE);

            Bitmap bitmapScaled = Bitmap.createScaledBitmap(imageBitmap, 100, 100, true);
            String result = imageClassifier.classify(bitmapScaled);

            results.add(result);

            showIngredients(results);
        }
    }

    private void showIngredients(List<String> results) {
        if(results.size() == 1){
            resultView.setText("");
            for (int j = 0; j < results.size(); j++) {
                resultView.append("Ingredient " + (j+1) +":  " + results.get(j) + "\n" + "\n");
            }
            deleteButton1.setVisibility(View.VISIBLE);
        }
        else if(results.size()==2){
            resultView.setText("");
            for (int j = 0; j < results.size(); j++) {
                resultView.append("Ingredient " + (j+1) +":  " + results.get(j) + "\n" + "\n");
            }
            deleteButton1.setVisibility(View.VISIBLE);
            deleteButton2.setVisibility(View.VISIBLE);
        }
        else{
            resultView.setText("");
            for (int j = 0; j < results.size(); j++) {
                resultView.append("Ingredient " + (j+1) +":  " + results.get(j) + "\n" + "\n");
            }
            deleteButton1.setVisibility(View.VISIBLE);
            deleteButton2.setVisibility(View.VISIBLE);
            deleteButton3.setVisibility(View.VISIBLE);
        }
    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this, "Camera Permission Required", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean hasPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public void cuisineToggle(View view) {

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
