package com.example.milkyway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewCustomersActivity extends AppCompatActivity {

    private ListView customersListView;
    private FirebaseFirestore db;
    private ArrayList<String> customersList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> customerIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);

        customersListView = findViewById(R.id.customersListView);
        db = FirebaseFirestore.getInstance();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customersList);
        customersListView.setAdapter(adapter);

        loadCustomers();

        customersListView.setOnItemClickListener((parent, view, position, id) -> {
            String customerId = customerIds.get(position);
            showOptionsDialog(customerId);
        });
    }

    private void loadCustomers() {
        db.collection("customers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String customerDetails = "Name: " + document.getString("name") +
                                    "\nAddress: " + document.getString("address") +
                                    "\nSubscription: " + document.getString("subscription");
                            customersList.add(customerDetails);
                            customerIds.add(document.getId());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ViewCustomersActivity.this, "Failed to load customers", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showOptionsDialog(String customerId) {
        new AlertDialog.Builder(this)
                .setTitle("Select Action")
                .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                    switch (which) {
                        case 0: // Edit
                            Intent intent = new Intent(ViewCustomersActivity.this, EditCustomerActivity.class);
                            intent.putExtra("customerId", customerId);
                            startActivity(intent);
                            break;
                        case 1: // Delete
                            deleteCustomer(customerId);
                            break;
                    }
                })
                .show();
    }

    private void deleteCustomer(String customerId) {
        db.collection("customers").document(customerId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ViewCustomersActivity.this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                    customersList.clear();
                    customerIds.clear();
                    loadCustomers();
                })
                .addOnFailureListener(e -> Toast.makeText(ViewCustomersActivity.this, "Failed to delete customer", Toast.LENGTH_SHORT).show());
    }
}
