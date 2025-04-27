package com.example.snapjer1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

public class AdminDeleteRecipePage extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    Button searchBtn, deleteBtn;
    EditText recipeName;
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_recipe_page);
        recipeName = findViewById(R.id.recipe_name);
        resultView = findViewById(R.id.resultView);
        searchBtn = findViewById(R.id.searchBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth = FirebaseAuth.getInstance();
                fstore = FirebaseFirestore.getInstance();
                String name = recipeName.getText().toString().trim();

                CollectionReference allRecipeName = fstore.collection("Recipe");

                allRecipeName.orderBy("recipeName").startAt(name).endAt(name + "\uf8ff").limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        String id = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Recipe recipe = documentSnapshot.toObject(Recipe.class);
                            id = documentSnapshot.getId();
                            String recipeName = recipe.getRecipeName();

                            data += recipeName + "\n";
                        }
                        resultView.setText(data);

                        if(!data.equals("")){
                            deleteBtn.setVisibility(View.VISIBLE);
                        }
                        else{
                            resultView.setText("No recipe found.");
                        }

                        final String finalId = id;
                        deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fstore.collection("Recipe").document(finalId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AdminDeleteRecipePage.this, "Recipe deleted.", Toast.LENGTH_SHORT).show();
                                        deleteBtn.setVisibility(View.GONE);
                                        resultView.setText("");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminDeleteRecipePage.this, "Error deleting recipe.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminDeleteRecipePage.this, "Error getting data.", Toast.LENGTH_SHORT).show();
                    }
                });


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
}
