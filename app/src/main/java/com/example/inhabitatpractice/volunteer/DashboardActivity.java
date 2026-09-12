package com.example.inhabitatpractice.volunteer;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.adapters.EventAdapter;
import com.example.inhabitatpractice.helpers.DBHelper;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private EventAdapter eventsAdapter;
    private EventAdapter myEventsAdapter;
    private DBHelper dbHelper;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard2);
        userId = getIntent().getLongExtra("user_id", 0);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(getApplicationContext());
        RecyclerView eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventAdapter(this, new ArrayList<>(), true, userId);
        eventsRecyclerView.setAdapter(eventsAdapter);

        RecyclerView myEventsRecyclerView = findViewById(R.id.myEventsRecyclerView);
        myEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myEventsAdapter = new EventAdapter(this, new ArrayList<>(), true, userId);
        myEventsRecyclerView.setAdapter(myEventsAdapter);

        findViewById(R.id.logout).setOnClickListener(v -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (eventsAdapter != null) {
            eventsAdapter.list.clear();
            eventsAdapter.list.addAll(dbHelper.getEvents());
            eventsAdapter.notifyDataSetChanged();

            myEventsAdapter.list.clear();
            myEventsAdapter.list.addAll(dbHelper.getJoinedEvents(userId));
            myEventsAdapter.notifyDataSetChanged();
        }
    }

    private void logout() {
        Intent intent = new Intent(this, com.example.inhabitatpractice.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
