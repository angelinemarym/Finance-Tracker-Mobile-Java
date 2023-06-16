package com.project.financetracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.project.financetracker.helper.DBHelper;
import com.project.financetracker.model.TransactionModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Locale;


public class TransactionRepository extends DBHelper implements ITransactionRepository {
    private static final String TABLE_NAME = "transactions";
    public TransactionRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public TransactionModel getById(int id) throws Exception {
        TransactionModel model;
        String selectStatement = "SELECT * FROM transactions WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);
        if(cursor.moveToFirst()) {
            String dateTime = cursor.getString(4);
            model = new TransactionModel(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), Date.valueOf(dateTime));
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
            do {
                Log.i("test: ", cursor.getString(4));
                models.add(new TransactionModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        Date.valueOf(cursor.getString(4))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return models;
    }

    @Override
    public List<TransactionModel> getByDateRange(Date start, Date end) throws ParseException {
        List<TransactionModel> models = new ArrayList<>();

        String selectStatement = String.format(Locale.ENGLISH, "SELECT * FROM %s WHERE createdAt >= '%s' AND createdAt <= '%s'", TABLE_NAME, start.toString(), end.toString());

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);
        if (cursor.moveToFirst()) {
            do {
                models.add(new TransactionModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        Date.valueOf(cursor.getString(4))));
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

    @Override
    public double getExpenseByDate(Date date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String stmt = "SELECT SUM(amount) AS amount FROM " + TABLE_NAME + " WHERE amount < 0 AND createdAt = " + date;

        Cursor cursor = db.rawQuery(stmt, null);
        if (cursor.moveToFirst()) {
            double amount = cursor.getDouble(0);
            cursor.close();

            return amount;
        } else {
            return 0;
        }
    }

}
