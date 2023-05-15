package com.project.financetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Transaction> transactions;
    private TransactionAdapter transactionAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactions = new ArrayList<>();
        transactions.add(new Transaction("Weekend Budget", 1000000));
        transactions.add(new Transaction("Bananas", -120000));
        transactions.add(new Transaction("Gasoline", -200000));
        transactions.add(new Transaction("Breakfast", -30000));
        transactions.add(new Transaction("Water bottle", -5000));
        transactions.add(new Transaction("Sunscreen", -50000));
        transactions.add(new Transaction("Car parking", -10000));

        transactionAdapter = new TransactionAdapter(transactions);
        linearLayoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerView = findViewById(R.id.transactions_recyclerview);
        recyclerView.setAdapter(transactionAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        updateDashboard();

        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.addButton);
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            startActivity(intent);
        });
    }

    private void updateDashboard (){
        double totalAmount = 0;
        double incomeAmount = 0;
        double expenseAmount = 0;

        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
            if (transaction.getAmount() > 0) {
                incomeAmount += transaction.getAmount();
            } else {
                expenseAmount += transaction.getAmount();
            }
        }

        TextView balance = findViewById(R.id.balance);
        TextView income = findViewById(R.id.income);
        TextView expense = findViewById(R.id.expense);

        balance.setText(String.format("$ %.0f", totalAmount));
        income.setText(String.format("$ %.0f", incomeAmount));
        expense.setText(String.format("$ %.0f", expenseAmount));
    }
}