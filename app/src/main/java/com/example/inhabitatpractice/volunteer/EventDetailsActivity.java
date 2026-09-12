package com.example.inhabitatpractice.volunteer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private DBHelper dbHelper;
    private long eventId;
    private long userId;
    private TextView joinAction;
    private VolunteerAdapter volunteerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details2);
        dbHelper = new DBHelper(getApplicationContext());
        eventId = getIntent().getLongExtra("event_id", 0);
        userId = getIntent().getLongExtra("user_id", 0);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        showEventDetails();
        joinAction = findViewById(R.id.joinAction);
        joinAction.setOnClickListener(v -> toggleEventJoin());
        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView volunteersRecyclerView = findViewById(R.id.volunteersRecyclerView);
        volunteersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        volunteerAdapter = new VolunteerAdapter(new ArrayList<>());
        volunteersRecyclerView.setAdapter(volunteerAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (joinAction != null) updateJoinAction();
        refreshVolunteerList();
    }

    private void updateJoinAction() {
        joinAction.setText(dbHelper.isEventJoined(eventId, userId) ? "Unjoin" : "Join");
    }

    private void toggleEventJoin() {
        boolean alreadyJoined = dbHelper.isEventJoined(eventId, userId);
        boolean changed = alreadyJoined
                ? dbHelper.unjoinEvent(eventId, userId)
                : dbHelper.joinEvent(eventId, userId);

        if (!changed) {
            Toast.makeText(this, alreadyJoined ? "Unable to unjoin event." : "Event is full.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, alreadyJoined ? "You left the event." : "You joined the event.", Toast.LENGTH_SHORT).show();
        updateJoinAction();
        refreshEventCount();
        refreshVolunteerList();
    }

    private void refreshEventCount() {
        for (com.example.inhabitatpractice.models.Event event : dbHelper.getEvents()) {
            if (event.id == eventId) {
                ((TextView) findViewById(R.id.eventVolunteers)).setText(
                        "Volunteers: " + event.volunteersJoined + "/" + event.volunteersNeeded);
                return;
            }
        }
    }

    private void refreshVolunteerList() {
        if (volunteerAdapter == null) return;
        volunteerAdapter.volunteers.clear();
        volunteerAdapter.volunteers.addAll(dbHelper.getEventVolunteers(eventId));
        volunteerAdapter.notifyDataSetChanged();
    }
}
