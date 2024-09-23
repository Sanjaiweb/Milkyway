package com.example.milkyway;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditCustomerActivity extends AppCompatActivity {

    private EditText editCustomerNameInput, editCustomerAddressInput, editSubscriptionInput;
    private Button saveCustomerButton;
    private FirebaseFirestore db;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        db = FirebaseFirestore.getInstance();

        editCustomerNameInput = findViewById(R.id.editCustomerNameInput);
        editCustomerAddressInput = findViewById(R.id.editCustomerAddressInput);
        editSubscriptionInput = findViewById(R.id.editSubscriptionInput);
        saveCustomerButton = findViewById(R.id.saveCustomerButton);

        customerId = getIntent().getStringExtra("customerId");

        loadCustomerDetails(customerId);

        saveCustomerButton.setOnClickListener(v -> updateCustomerDetails());
    }

    private void loadCustomerDetails(String customerId) {
        db.collection("customers").document(customerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editCustomerNameInput.setText(documentSnapshot.getString("name"));
                        editCustomerAddressInput.setText(documentSnapshot.getString("address"));
                        editSubscriptionInput.setText(documentSnapshot.getString("subscription"));
                    }
                });
    }

    private void updateCustomerDetails() {
        String name = editCustomerNameInput.getText().toString();
        String address = editCustomerAddressInput.getText().toString();
        String subscription = editSubscriptionInput.getText().toString();

        if (name.isEmpty() || address.isEmpty() || subscription.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> customerUpdates = new HashMap<>();
        customerUpdates.put("name", name);
        customerUpdates.put("address", address);
        customerUpdates.put("subscription", subscription);

        db.collection("customers").document(customerId)
                .update(customerUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditCustomerActivity.this, "Customer updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditCustomerActivity.this, "Failed to update customer", Toast.LENGTH_SHORT).show());
    }
}
