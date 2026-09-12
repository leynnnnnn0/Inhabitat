package com.example.inhabitatpractice.admin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.adapters.VolunteerAdapter;
import com.example.inhabitatpractice.helpers.DBHelper;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {

    TextView back;
    private DBHelper dbHelper;
    private long eventId;
    private VolunteerAdapter volunteerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);
        dbHelper = new DBHelper(getApplicationContext());
        eventId = getIntent().getLongExtra("event_id", 0);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showEventDetails();

        RecyclerView volunteersRecyclerView = findViewById(R.id.volunteersRecyclerView);
        volunteersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        volunteerAdapter = new VolunteerAdapter(new ArrayList<>());
        volunteersRecyclerView.setAdapter(volunteerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (volunteerAdapter != null) {
            volunteerAdapter.volunteers.clear();
            volunteerAdapter.volunteers.addAll(dbHelper.getEventVolunteers(eventId));
            volunteerAdapter.notifyDataSetChanged();
        }
    }

    private void showEventDetails() {
        String plant = getIntent().getStringExtra("plant");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        int volunteersNeeded = getIntent().getIntExtra("volunteers_needed", 0);
        int volunteersJoined = getIntent().getIntExtra("volunteers_joined", 0);

        ((ImageView) findViewById(R.id.eventImage)).setImageResource(R.drawable.default_1);
        ((TextView) findViewById(R.id.eventPlantTitle)).setText(plant);
        ((TextView) findViewById(R.id.eventName)).setText(name);
        ((TextView) findViewById(R.id.eventDescription)).setText(description);
        ((TextView) findViewById(R.id.eventDate)).setText("Date: " + date);
        ((TextView) findViewById(R.id.eventPlant)).setText("Plant: " + plant);
        ((TextView) findViewById(R.id.eventStatus)).setText("Status: Ongoing");
        ((TextView) findViewById(R.id.eventVolunteers)).setText(
                "Volunteers: " + volunteersJoined + "/" + volunteersNeeded);
    }
}
