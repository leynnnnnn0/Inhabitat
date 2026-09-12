package com.example.inhabitatpractice;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.inhabitatpractice.helpers.CrudHelper;
import com.example.inhabitatpractice.helpers.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    TextView name, username, dateOfBirth, password, confirmPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);

        register.setOnClickListener(v -> {
            SQLiteDatabase db = new DBHelper(this).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name.getText().toString().trim());
            contentValues.put("username", username.getText().toString().trim());
            contentValues.put("dateOfBirth", dateOfBirth.getText().toString().trim());
            contentValues.put("password", password.getText().toString().trim());
            contentValues.put("role", "volunteer");
            long result = CrudHelper.insert(db, "users", contentValues);
            if(result != -1){
                Toast.makeText(this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

            db.close();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}