package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button addCustomerButton, viewCustomersButton, viewDeliveriesButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addCustomerButton = findViewById(R.id.addCustomerButton);
        viewCustomersButton = findViewById(R.id.viewCustomersButton);
        viewDeliveriesButton = findViewById(R.id.viewDeliveriesButton);
        logoutButton = findViewById(R.id.logoutButton);

        addCustomerButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AddCustomerActivity.class));
        });

        viewCustomersButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, ViewCustomersActivity.class));
        });

        viewDeliveriesButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, ViewDeliveriesActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminDashboardActivity.this,Login.class));
            finish();
        });
    }
}
