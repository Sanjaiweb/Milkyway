package com.example.milkyway;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText customerNameInput, customerAddressInput, subscriptionInput;
    private Button addCustomerButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        db = FirebaseFirestore.getInstance();

        customerNameInput = findViewById(R.id.customerNameInput);
        customerAddressInput = findViewById(R.id.customerAddressInput);
        subscriptionInput = findViewById(R.id.subscriptionInput);
        addCustomerButton = findViewById(R.id.addCustomerButton);

        addCustomerButton.setOnClickListener(v -> addCustomerToFirestore());
    }

    private void addCustomerToFirestore() {
        String name = customerNameInput.getText().toString();
        String address = customerAddressInput.getText().toString();
        String subscription = subscriptionInput.getText().toString();

        if (name.isEmpty() || address.isEmpty() || subscription.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> customer = new HashMap<>();
        customer.put("name", name);
        customer.put("address", address);
        customer.put("subscription", subscription);
        customer.put("paymentStatus", false);  // Default payment status

        db.collection("customers")
                .add(customer)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddCustomerActivity.this, "Customer Added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddCustomerActivity.this, "Failed to add customer", Toast.LENGTH_SHORT).show());
    }
}
