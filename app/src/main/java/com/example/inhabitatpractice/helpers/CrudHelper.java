package com.example.inhabitatpractice.helpers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class CrudHelper {
    public static long insert(SQLiteDatabase db, String tableName, ContentValues contentValues){
        return db.insert(tableName, null, contentValues);
    }
}
