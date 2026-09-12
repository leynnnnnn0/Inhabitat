package com.example.inhabitatpractice;

import android.content.Intent;
import android.database.Cursor;
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

import com.example.inhabitatpractice.helpers.DBHelper;

public class LoginActivity extends AppCompatActivity {
    TextView username, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> {
            SQLiteDatabase db = new DBHelper(this).getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?",
                    new String[]{username.getText().toString().trim(), password.getText().toString().trim()});
            
            if(cursor.moveToFirst()){
                Toast.makeText(this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                long userId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                Intent intent = new Intent(this, "admin".equalsIgnoreCase(role)
                        ? com.example.inhabitatpractice.admin.DashboardActivity.class
                        : com.example.inhabitatpractice.volunteer.DashboardActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, "Incorrect Credentials.", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
