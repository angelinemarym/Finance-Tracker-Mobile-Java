package com.project.financetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.financetracker.adapter.TransactionAdapter;
import com.project.financetracker.constants.Constant;
import com.project.financetracker.model.TransactionModel;
import com.project.financetracker.repository.ExpenseRepository;
import com.project.financetracker.repository.IExpenseRepository;
import com.project.financetracker.repository.ITransactionRepository;
import com.project.financetracker.repository.TransactionRepository;

import java.text.ParseException;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private final ITransactionRepository transactionRepository = new TransactionRepository(MainActivity.this, Constant.DB_NAME, null, Constant.VERSION);
    private final IExpenseRepository expenseRepository = new ExpenseRepository(MainActivity.this, Constant.DB_NAME, null, Constant.VERSION);
    private TransactionAdapter transactionAdapter;
    private List<TransactionModel> transactionModels;
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    private final Calendar now = Calendar.getInstance();
    private Date startDate = null, endDate = null;
    private String dateRange = null;
    private Button expenseLimitBtn;
    private AlertDialog dialog;
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
        expenseLimitBtn = findViewById(R.id.expenseLimitBtn);

        try {
            if(startDate != null && endDate != null){
                transactionModels = transactionRepository.getByDateRange(startDate, endDate);
                transactionDateRange.setText(dateRange);
            } else{
                transactionModels = transactionRepository.getAll(0, 10);
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
                                newData = transactionRepository.getByDateRange(startDate, endDate);
                                transactionDateRange.setText(dateRange);
                            } else{
                                newData = transactionRepository.getAll(transactionModels.get(lastIndex).getId(), 10);
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
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                transactionRepository.delete(transactionModels.get(viewHolder.getAdapterPosition()).getId());
                showSnackbar();
                updateDashboard();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Expense Limit");
        // Inflate custom dialog view
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        EditText expenseLimit = view.findViewById(R.id.expense_limit);
        Button submitExpenseLimit = view.findViewById(R.id.submit_expense_limit);

        // TODO: save the expense limit to DB and add alert when the daily expense has exceeded the limit.
        submitExpenseLimit.setOnClickListener(new View.OnClickListener() {

            final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("da", "DK"));
            @Override
            public void onClick(View v) {
                Editable expenseLimitValue = expenseLimit.getText();
                if (expenseLimitValue == null) {
                    dialog.dismiss();
                } else {
                    expenseRepository.setExpenseLimit(Double.parseDouble(expenseLimitValue.toString()));
                }
                expenseLimitBtn.setText("Expense limit: " + numberFormat.format(Integer.parseInt(expenseLimitValue.toString())));
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();

        expenseLimitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
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
        String balanceStr = nf.format(transactionRepository.getBalance());
        String incomeStr = nf.format(transactionRepository.getIncome());
        String expenseStr = nf.format(transactionRepository.getExpense());

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