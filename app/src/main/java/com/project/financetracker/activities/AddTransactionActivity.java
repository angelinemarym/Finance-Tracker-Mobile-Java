package com.project.financetracker.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.financetracker.R;
import com.project.financetracker.constants.Constant;
import com.project.financetracker.repository.ExpenseRepository;
import com.project.financetracker.repository.IExpenseRepository;
import com.project.financetracker.repository.ITransactionRepository;
import com.project.financetracker.repository.TransactionRepository;

import java.sql.Date;

public class AddTransactionActivity extends AppCompatActivity {
    boolean success = false;
    int checkedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        TextInputEditText labelInput = (TextInputEditText) findViewById(R.id.labelInput);
        TextInputEditText amountInput = (TextInputEditText) findViewById(R.id.amountInput);
        TextInputEditText descriptionInput = (TextInputEditText) findViewById(R.id.descriptionInput);
        TextInputLayout labelLayout = (TextInputLayout) findViewById(R.id.labelLayout);
        TextInputLayout amountLayout = (TextInputLayout) findViewById(R.id.amountLayout);
        TextInputLayout descriptionLayout = (TextInputLayout) findViewById(R.id.descriptionLayout);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        RadioButton radioButtonExpense = (RadioButton) findViewById(R.id.radioButtonExpense);

        Button addTransactionBtn = (Button) findViewById(R.id.addTransactionButton);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        labelInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0)
                    labelLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0)
                    amountLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                checkedId = checkId;

            }
        });

        addTransactionBtn.setOnClickListener(view -> {
            double amount = Double.parseDouble(amountInput.getText().toString().isEmpty() ? "0" :amountInput.getText().toString());
            String label = labelInput.getText().toString();
            String description = descriptionInput.getText().toString();

            if (label.isEmpty())
                labelLayout.setError("Please enter a valid label");
            if (amount == 0)
                amountLayout.setError("Please enter a valid amount");

            ITransactionRepository transactionRepository = new TransactionRepository(AddTransactionActivity.this, Constant.DB_NAME, null, Constant.VERSION);
            IExpenseRepository expenseRepository = new ExpenseRepository(AddTransactionActivity.this, Constant.DB_NAME, null, Constant.VERSION);

            if (checkedId == R.id.radioButtonExpense) {
                java.util.Date today = new java.util.Date();
                Date currDate = new Date(today.getTime());
                double todayExpense = transactionRepository.getExpenseByDate(currDate);
                double todayExpenseLimit;
                try{
                    todayExpenseLimit = expenseRepository.getExpenseLimit();
                    System.out.println("expense limit: " + todayExpenseLimit);
                    System.out.println("expense: " + todayExpense);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if(todayExpenseLimit > 0 && todayExpense * -1 + amount > todayExpenseLimit){
                    alert.setTitle("You've reached the expense limit!");
                    alert.setMessage("Do you want to add this item anyway?");
                    alert.setPositiveButton("Yes", (dialog, which) -> {
                        success = transactionRepository.create(label, amount * -1, description);
                        dialog.dismiss();
                        finish();
                    });
                    alert.setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                        success = false;
                    });
                    alert.create().show();
                } else {
                    success = transactionRepository.create(label, amount * -1, description);
                    finish();
                }
            }
            else if (checkedId == R.id.radioButtonIncome) {
                success = transactionRepository.create(label, amount, description);
                finish();
            }
            if (!success) {
                Toast.makeText(AddTransactionActivity.this, "Failed to create new transactions", Toast.LENGTH_SHORT).show();
                return;
            } else{
                finish();
            }
        });

        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeButton);

        closeBtn.setOnClickListener(view -> finish());
    }
}
