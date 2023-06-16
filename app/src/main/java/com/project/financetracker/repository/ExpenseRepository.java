package com.project.financetracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        cv.put("expenseLimit", expenseLimit);

        db.update(TABLE_NAME, cv, "", null);

        return expenseLimit;
    }

    @Override
    public double getExpenseLimit() throws Exception {
        String selectStmt = "SELECT expenseLimit";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStmt, null);
        if(cursor.moveToFirst()) {
            String dateTime = cursor.getString(4);
            return cursor.getDouble(0);
        } else {
            cursor.close();
            throw new Exception();
        }
    }
}
