package com.project.financetracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.project.financetracker.model.TransactionModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TransactionRepository extends SQLiteOpenHelper implements Repository {
    private static final String TABLE_NAME = "transactions";
    public TransactionRepository(@Nullable Context context) {
        super(context, "transaction.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, label VARCHAR(255) NOT NULL, amount REAL NOT NULL DEFAULT 0, description TEXT, createdAt DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public TransactionModel getById(int id) throws Exception {
        TransactionModel model;
        String selectStatement = "SELECT * FROM transactions WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);
        if(cursor.moveToFirst()) {
            String dateTime = cursor.getString(4);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            model = new TransactionModel(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), dateFormat.parse(dateTime));
        } else {
            cursor.close();
            throw new Exception();
        }

        cursor.close();

        return model;
    }

    @Override
    public List<TransactionModel> getAll(int lastId, int limit) throws ParseException {
        List<TransactionModel> models = new ArrayList<>();

        String whereIdClause = lastId != 0 ? "WHERE id > " + lastId : "";
        String selectStatement = String.format(Locale.ENGLISH, "SELECT * FROM %s %s LIMIT %d", TABLE_NAME, whereIdClause, limit);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);
        if (cursor.moveToFirst()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            do {
                models.add(new TransactionModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        dateFormat.parse(cursor.getString(4))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return models;
    }

    @Override
    public List<TransactionModel> getByDateRange(Date start, Date end) throws ParseException {
        List<TransactionModel> models = new ArrayList<>();

        String selectStatement = String.format(Locale.ENGLISH, "SELECT * FROM %s WHERE createdAt >= %s AND createdAt <= %s", TABLE_NAME, start, end);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);
        if (cursor.moveToFirst()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            do {
                models.add(new TransactionModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        dateFormat.parse(cursor.getString(4))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return models;
    }
    @Override
    public boolean create(String label, double amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("label", label);
        cv.put("amount", amount);
        cv.put("description", description);

        long success = db.insert(TABLE_NAME, null, cv);
        return success != -1;
    }

    @Override
    public void update(int id, TransactionModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("label", model.getLabel());
        cv.put("amount", model.getAmount());
        cv.put("description", model.getDescription());

        db.update(TABLE_NAME, cv, "id = " + id, null);
    }

    @Override
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "id = " + id, null);
    }

    @Override
    public double getBalance() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(amount) AS balance FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            double amount = cursor.getDouble(0);
            cursor.close();

            return amount;
        } else {
            return 0;
        }
    }

    @Override
    public double getIncome() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(amount) AS amount FROM " + TABLE_NAME + " WHERE amount > 0", null);
        if (cursor.moveToFirst()) {
            double amount = cursor.getDouble(0);
            cursor.close();

            return amount;
        } else {
            return 0;
        }
    }

    @Override
    public double getExpense() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(amount) AS amount FROM " + TABLE_NAME + " WHERE amount < 0", null);
        if (cursor.moveToFirst()) {
            double amount = cursor.getDouble(0);
            cursor.close();

            return amount;
        } else {
            return 0;
        }
    }

}
