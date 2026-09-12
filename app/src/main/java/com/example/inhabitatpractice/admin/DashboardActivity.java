package com.example.inhabitatpractice.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.adapters.EventAdapter;
import com.example.inhabitatpractice.adapters.PlantAdapter;
import com.example.inhabitatpractice.helpers.DBHelper;
import com.example.inhabitatpractice.models.Plant;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private EventAdapter eventAdapter;
    private DBHelper dbHelper;

    public TextView create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getApplicationContext());
        RecyclerView eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, new ArrayList<>());
        eventsRecyclerView.setAdapter(eventAdapter);

        RecyclerView plantsRecyclerView = findViewById(R.id.plantsRecyclerView);
        plantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plantsRecyclerView.setAdapter(new PlantAdapter(Plant.getPlants()));

        create = findViewById(R.id.create);
        create.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateEventActivity.class));
        });
        findViewById(R.id.logout).setOnClickListener(v -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (eventAdapter != null) {
            eventAdapter.list.clear();
            eventAdapter.list.addAll(dbHelper.getEvents());
            eventAdapter.notifyDataSetChanged();
        }
    }

    private void logout() {
        Intent intent = new Intent(this, com.example.inhabitatpractice.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
