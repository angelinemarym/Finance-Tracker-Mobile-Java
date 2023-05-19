package com.project.financetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.financetracker.adapter.TransactionAdapter;
import com.project.financetracker.model.TransactionModel;
import com.project.financetracker.repository.Repository;
import com.project.financetracker.repository.TransactionRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final Repository repository = new TransactionRepository(MainActivity.this);
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMainPage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadMainPage();
    }

    private void loadMainPage() {
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.transactions_recyclerview);

        try {
            List<TransactionModel> transactionModels = repository.getAll(0, 10);
//
            transactionAdapter = new TransactionAdapter(transactionModels);
            recyclerView.setAdapter(transactionAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(1)) {
                        int lastIndex = transactionModels.size() - 1;

                        try {
                            List<TransactionModel> newData = repository.getAll(transactionModels.get(lastIndex).getId(), 10);
                            transactionModels.addAll(newData);

                            transactionAdapter.setData(transactionModels);
                        } catch (ParseException e) {
                            Toast.makeText(null, "Error getting transaction data: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            updateDashboard();

            FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.addButton);
            addBtn.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            });
        } catch(ParseException e) {
            Toast.makeText(MainActivity.this, "Error getting transaction data: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    private void updateDashboard (){
        TextView balance = findViewById(R.id.balance);
        TextView income = findViewById(R.id.income);
        TextView expense = findViewById(R.id.expense);

        balance.setText(String.format(Locale.ENGLISH, "$ %.0f", repository.getBalance()));
        income.setText(String.format(Locale.ENGLISH, "$ %.0f", repository.getIncome()));
        expense.setText(String.format(Locale.ENGLISH, "$ %.0f", repository.getExpense()));
    }
}