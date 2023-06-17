package com.project.financetracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.project.financetracker.helper.DBHelper;

public class ExpenseRepository extends DBHelper implements IExpenseRepository {
    private static final String TABLE_NAME = "expenseLimits";
    public ExpenseRepository(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public double setExpenseLimit(double expenseLimit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("expenseLimit", expenseLimit);

        long success = db.insert(TABLE_NAME, null, cv);
        if(success == -1) {
            cv.remove("id");
            db.update(TABLE_NAME, cv, null, null);
        }

        return expenseLimit;
    }

    @Override
    public double getExpenseLimit() throws Exception {
        double limit = 0;
//        String selectStmt = "SELECT * FROM expenseLimits LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{"expenseLimit"}, null, null, null, null, null);
        System.out.println(cursor.getCount());
        if(cursor.moveToNext()) {
            limit = cursor.getDouble(0);
        } else {
            cursor.close();
            throw new Exception();
        }

        cursor.close();

        return limit;
    }
}
