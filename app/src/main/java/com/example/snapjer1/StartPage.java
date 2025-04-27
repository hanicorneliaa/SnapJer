package com.example.snapjer1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StartPage extends AppCompatActivity {

    private Button browse;
    private Button snap;
    private Button western, malay, vegetarian, indian, chinese, vegan;
    private FirebaseAuth fAuth;
    private TextView nickname;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        browse = findViewById(R.id.browse);
        snap = findViewById(R.id.snap);
        western = findViewById(R.id.westernBtn);
        malay = findViewById(R.id.malayBtn);
        vegetarian = findViewById(R.id.vegetarianBtn);
        indian = findViewById(R.id.indianBtn);
        chinese = findViewById(R.id.chineseBtn);
        vegan = findViewById(R.id.veganBtn);
        nickname = findViewById(R.id.nickname);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String userID = fAuth.getCurrentUser().getUid();
        //Query
        firebaseFirestore.collection("User").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                nickname.setText(snapshot.getString("fullName"));
            }
        });

        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SnapGuide.class));
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainPage.class));
            }
        });

        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("cuisine", 1);
                startActivity(intent);
            }
        });

        malay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("cuisine", 2);
                startActivity(intent);
            }
        });

        vegetarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("cuisine", 3);
                startActivity(intent);
            }
        });

        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("cuisine", 4);
                startActivity(intent);
            }
        });

        indian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("cuisine", 5);
                startActivity(intent);
            }
        });

        vegan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("cuisine", 6);
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
