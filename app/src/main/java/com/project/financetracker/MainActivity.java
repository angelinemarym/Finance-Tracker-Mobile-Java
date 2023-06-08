package com.project.financetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.financetracker.adapter.TransactionAdapter;
import com.project.financetracker.model.TransactionModel;
import com.project.financetracker.repository.Repository;
import com.project.financetracker.repository.TransactionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private final Repository repository = new TransactionRepository(MainActivity.this);
    private TransactionAdapter transactionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<TransactionModel> transactionModels;
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    private Calendar now = Calendar.getInstance();
    private Date startDate = null, endDate = null;
    private String dateRange = null;
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
        TextView transactionDateRange = findViewById(R.id.transactions_dateRange);

        builder.setSelection(new androidx.core.util.Pair<>(now.getTimeInMillis(), now.getTimeInMillis()));
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        try {
            if(startDate != null && endDate != null){
                transactionModels = repository.getByDateRange(startDate, endDate);
                transactionDateRange.setText(dateRange);
            } else{
                transactionModels = repository.getAll(0, 10);
                transactionDateRange.setText("All");
            }

            transactionAdapter = new TransactionAdapter(transactionModels, this);
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
//                          TODO: Add pagination for getByDateRange function
                            List<TransactionModel> newData;
                            if(startDate != null && endDate != null){
                                newData = repository.getByDateRange(startDate, endDate);
                                transactionDateRange.setText(dateRange);
                            } else{
                                newData = repository.getAll(transactionModels.get(lastIndex).getId(), 10);
                                transactionDateRange.setText("All");
                            }
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

            ImageButton filterBtn = (ImageButton) findViewById(R.id.filter_btn);
            filterBtn.setOnClickListener(view -> picker.show(this.getSupportFragmentManager(), picker.toString()));
        } catch(ParseException e) {
            Toast.makeText(MainActivity.this, "Error getting transaction data: " + e, Toast.LENGTH_SHORT).show();
        }

        picker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        startDate = new Date(picker.getSelection().first);
                        endDate = new Date(picker.getSelection().second);
                        dateRange = picker.getHeaderText();
                        loadMainPage();
                    }
                });

        // Swipe to remove
        linearLayoutManager = new LinearLayoutManager(this);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Repository transactionsRepository = new TransactionRepository(MainActivity.this);
                transactionsRepository.delete(transactionModels.get(viewHolder.getAdapterPosition()).getId());
                showSnackbar();
                updateDashboard();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showSnackbar() {
        View view = findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(view, "Transaction deleted!", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white)).show();
    }

    private void updateDashboard (){
        TextView balance = findViewById(R.id.balance);
        TextView income = findViewById(R.id.income);
        TextView expense = findViewById(R.id.expense);

        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        String balanceStr = nf.format(repository.getBalance());
        String incomeStr = nf.format(repository.getIncome());
        String expenseStr = nf.format(repository.getExpense());

        balance.setText("Rp" + balanceStr);
        income.setText("Rp" + incomeStr);
        expense.setText("Rp" + expenseStr);
    }

    @Override
    public void onItemClick(int position) {
        Intent edit_intent = new Intent(MainActivity.this, EditTransactionActivity.class);
        edit_intent.putExtra("id", transactionModels.get(position).getId());
        edit_intent.putExtra("label", transactionModels.get(position).getLabel());
        edit_intent.putExtra("amount", transactionModels.get(position).getAmount());
        edit_intent.putExtra("description", transactionModels.get(position).getDescription());
        edit_intent.putExtra("createdAt", transactionModels.get(position).getCreatedAt());

        startActivity(edit_intent);
    }
}