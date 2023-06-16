package com.project.financetracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.financetracker.constants.Constant;
import com.project.financetracker.repository.ExpenseRepository;
import com.project.financetracker.repository.IExpenseRepository;
import com.project.financetracker.repository.ITransactionRepository;
import com.project.financetracker.repository.TransactionRepository;

import java.sql.Date;

public class AddTransactionActivity extends AppCompatActivity {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                addTransactionBtn.setOnClickListener(view -> {
                    final boolean[] success = {false};
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
//                      TODO: add expense limit validation here
                        java.util.Date today = new java.util.Date();
                        Date currDate = new Date(today.getTime());
                        double todayExpense = transactionRepository.getExpenseByDate(currDate);
                        double todayExpenseLimit;
                        try{
                            todayExpenseLimit = expenseRepository.getExpenseLimit();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        if(todayExpense > todayExpenseLimit){
                            builder.setTitle("You have reached your expense limit!")
                                    .setMessage("Do you want to add the item anyway?")
                                    .setCancelable(true)
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            success[0] = transactionRepository.create(label, amount * -1, description);
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            success[0] = transactionRepository.create(label, amount * -1, description);
                        }
                    }
                    else if (checkedId == R.id.radioButtonIncome) {
                        success[0] = transactionRepository.create(label, amount, description);
                    }
                    if (!success[0]) {
                        Toast.makeText(AddTransactionActivity.this, "Failed to create new transactions", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                });
            }
        });
        
        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeButton);

        closeBtn.setOnClickListener(view -> finish());
    }
}
