package com.example.inhabitatpractice.admin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.inhabitatpractice.R;
import com.example.inhabitatpractice.helpers.DBHelper;
import com.example.inhabitatpractice.models.Plant;

import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupPlantSpinner();
        findViewById(R.id.create).setOnClickListener(v -> createEvent());
    }

    private void setupPlantSpinner() {
        Spinner plantSpinner = findViewById(R.id.plantSpinner);
        List<Plant> plants = Plant.getPlants();

        ArrayAdapter<Plant> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                plants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantSpinner.setAdapter(adapter);
    }

    private void createEvent() {
        Spinner plantSpinner = findViewById(R.id.plantSpinner);
        EditText nameInput = findViewById(R.id.name);
        EditText descriptionInput = findViewById(R.id.description);
        EditText dateInput = findViewById(R.id.date);
        EditText volunteersNeededInput = findViewById(R.id.volunteersNeeded);

        Plant selectedPlant = (Plant) plantSpinner.getSelectedItem();
        String name = nameInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String volunteersNeededText = volunteersNeededInput.getText().toString().trim();

        if (selectedPlant == null || name.isEmpty() || description.isEmpty()
                || date.isEmpty() || volunteersNeededText.isEmpty()) {
            Toast.makeText(this, "Please complete all event fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        int volunteersNeeded;
        try {
            volunteersNeeded = Integer.parseInt(volunteersNeededText);
            if (volunteersNeeded < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            volunteersNeededInput.setError("Enter a number greater than 0");
            return;
        }

        ContentValues values = new ContentValues();
        values.put("plant", selectedPlant.getName());
        values.put("name", name);
        values.put("description", description);
        values.put("date", date);
        values.put("volunteers_needed", volunteersNeeded);
        values.put("volunteers_joined", 0);

        SQLiteDatabase db = new DBHelper(getApplicationContext()).getWritableDatabase();
        long eventId = db.insert("events", null, values);

        if (eventId == -1) {
            Toast.makeText(this, "Unable to create event.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Event created.", Toast.LENGTH_SHORT).show();
        nameInput.setText("");
        descriptionInput.setText("");
        dateInput.setText("");
        volunteersNeededInput.setText("");
    }
}
