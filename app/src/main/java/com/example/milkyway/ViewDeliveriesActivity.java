package com.example.milkyway;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewDeliveriesActivity extends AppCompatActivity {

    private ListView deliveriesListView;
    private FirebaseFirestore db;
    private ArrayList<String> deliveriesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deliveries);

        deliveriesListView = findViewById(R.id.deliveriesListView);
        db = FirebaseFirestore.getInstance();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deliveriesList);
        deliveriesListView.setAdapter(adapter);

        loadDeliveries();
    }

    private void loadDeliveries() {
        db.collection("deliveries")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String deliveryDetails = "Customer ID: " + document.get("customerId") +
                                    "\nDelivery Status: " + document.get("status");
                            deliveriesList.add(deliveryDetails);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ViewDeliveriesActivity.this, "Failed to load deliveries", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
