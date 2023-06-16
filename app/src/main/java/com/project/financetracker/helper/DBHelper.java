package com.project.financetracker.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + "transactions" + " (id INTEGER PRIMARY KEY AUTOINCREMENT, label VARCHAR(255) NOT NULL, amount REAL NOT NULL DEFAULT 0, description TEXT, createdAt DATE DEFAULT CURRENT_DATE)";
        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE " + "expenseLimits" + " (expenseLimit REAL DEFAULT 0)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
