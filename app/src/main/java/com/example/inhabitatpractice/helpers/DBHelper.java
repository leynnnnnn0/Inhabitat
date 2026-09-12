package com.example.inhabitatpractice.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inhabitatpractice.models.Event;
import com.example.inhabitatpractice.models.Volunteer;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String dbName = "ihabitat.db";
    private static final int DB_VERSION = 10;

    public DBHelper(Context context) {
        super(context, dbName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUsersTable(db);
        createEventsTable(db);
        createEventVolunteersTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS event_volunteers");
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    private void createUsersTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "username TEXT UNIQUE," +
                "dateOfBirth TEXT," +
                "role TEXT," +
                "password)");
        db.execSQL("INSERT INTO users (name, username, dateOfBirth, role, password) VALUES ('admin', 'admin', 'August 19, 2024', 'admin', 'password')");
        db.execSQL("INSERT INTO users (name, username, dateOfBirth, role, password) VALUES ('nathaniel', 'nathaniel', 'August 19, 2024', 'volunteer', 'password')");
    }

    private void createEventsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS events (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plant TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "volunteers_needed INTEGER NOT NULL, " +
                "volunteers_joined INTEGER NOT NULL DEFAULT 0)");
    }

    private void createEventVolunteersTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS event_volunteers (" +
                "event_id INTEGER NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "PRIMARY KEY (event_id, user_id), " +
                "FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");
    }

    public ArrayList<Event> getEvents() {
        return getEventsForUser(0, false);
    }

    public ArrayList<Volunteer> getEventVolunteers(long eventId) {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT users.id, users.name, users.username FROM users " +
                        "INNER JOIN event_volunteers ON users.id = event_volunteers.user_id " +
                        "WHERE event_volunteers.event_id = ? ORDER BY users.name ASC",
                new String[]{String.valueOf(eventId)})) {
            int idColumn = cursor.getColumnIndexOrThrow("id");
            int nameColumn = cursor.getColumnIndexOrThrow("name");
            int usernameColumn = cursor.getColumnIndexOrThrow("username");
            while (cursor.moveToNext()) {
                Volunteer volunteer = new Volunteer();
                volunteer.id = cursor.getLong(idColumn);
                volunteer.name = cursor.getString(nameColumn);
                volunteer.username = cursor.getString(usernameColumn);
                volunteers.add(volunteer);
            }
        }
        return volunteers;
    }

    public ArrayList<Event> getJoinedEvents(long userId) {
        return getEventsForUser(userId, true);
    }

    private ArrayList<Event> getEventsForUser(long userId, boolean joinedOnly) {
        ArrayList<Event> events = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                     joinedOnly
                             ? "SELECT events.* FROM events INNER JOIN event_volunteers " +
                               "ON events.id = event_volunteers.event_id " +
                               "WHERE event_volunteers.user_id = ? ORDER BY events.id DESC"
                             : "SELECT * FROM events ORDER BY id DESC",
                     joinedOnly ? new String[]{String.valueOf(userId)} : null)) {
            int idColumn = cursor.getColumnIndexOrThrow("id");
            int plantColumn = cursor.getColumnIndexOrThrow("plant");
            int nameColumn = cursor.getColumnIndexOrThrow("name");
            int descriptionColumn = cursor.getColumnIndexOrThrow("description");
            int dateColumn = cursor.getColumnIndexOrThrow("date");
            int neededColumn = cursor.getColumnIndexOrThrow("volunteers_needed");
            int joinedColumn = cursor.getColumnIndexOrThrow("volunteers_joined");

            while (cursor.moveToNext()) {
                Event event = new Event();
                event.id = cursor.getLong(idColumn);
                event.plant = cursor.getString(plantColumn);
                event.name = cursor.getString(nameColumn);
                event.description = cursor.getString(descriptionColumn);
                event.date = cursor.getString(dateColumn);
                event.volunteersNeeded = cursor.getInt(neededColumn);
                event.volunteersJoined = cursor.getInt(joinedColumn);
                events.add(event);
            }
        }

        return events;
    }

    public boolean isEventJoined(long eventId, long userId) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                     "SELECT 1 FROM event_volunteers WHERE event_id = ? AND user_id = ?",
                     new String[]{String.valueOf(eventId), String.valueOf(userId)})) {
            return cursor.moveToFirst();
        }
    }

    public boolean joinEvent(long eventId, long userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            if (isEventJoined(eventId, userId)) return false;
            db.execSQL("INSERT INTO event_volunteers (event_id, user_id) VALUES (?, ?)",
                    new Object[]{eventId, userId});
            db.execSQL("UPDATE events SET volunteers_joined = volunteers_joined + 1 " +
                    "WHERE id = ? AND volunteers_joined < volunteers_needed", new Object[]{eventId});
            if (db.compileStatement("SELECT changes()").simpleQueryForLong() == 0) return false;
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public boolean unjoinEvent(long eventId, long userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DELETE FROM event_volunteers WHERE event_id = ? AND user_id = ?",
                    new Object[]{eventId, userId});
            if (db.compileStatement("SELECT changes()").simpleQueryForLong() == 0) return false;
            db.execSQL("UPDATE events SET volunteers_joined = MAX(volunteers_joined - 1, 0) WHERE id = ?",
                    new Object[]{eventId});
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }
}
