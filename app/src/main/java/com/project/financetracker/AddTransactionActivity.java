package com.project.financetracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.financetracker.repository.Repository;
import com.project.financetracker.repository.TransactionRepository;

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
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioButtonExpense) {
                    addTransactionBtn.setOnClickListener(view -> {
                        double amount = Double.parseDouble(amountInput.getText().toString().isEmpty() ? "0" :amountInput.getText().toString());
                        String label = labelInput.getText().toString();
                        String description = descriptionInput.getText().toString();

                        if (label.isEmpty())
                            labelLayout.setError("Please enter a valid label");
                        if (amount == 0)
                            amountLayout.setError("Please enter a valid amount");

                        Repository transactionsRepository = new TransactionRepository(AddTransactionActivity.this);
                        boolean success = transactionsRepository.create(label, amount*-1, description);
                        if (!success) {
                            Toast.makeText(AddTransactionActivity.this, "Failed to create new transactions", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    });
                }
                else  if (checkedId == R.id.radioButtonIncome) {
                    addTransactionBtn.setOnClickListener(view -> {
                        double amount = Double.parseDouble(amountInput.getText().toString().isEmpty() ? "0" :amountInput.getText().toString());
                        String label = labelInput.getText().toString();
                        String description = descriptionInput.getText().toString();

                        if (label.isEmpty())
                            labelLayout.setError("Please enter a valid label");
                        if (amount == 0)
                            amountLayout.setError("Please enter a valid amount");

                        Repository transactionsRepository = new TransactionRepository(AddTransactionActivity.this);
                        boolean success = transactionsRepository.create(label, amount, description);
                        if (!success) {
                            Toast.makeText(AddTransactionActivity.this, "Failed to create new transactions", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    });
                }
            }
        });
        
        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeButton);

        closeBtn.setOnClickListener(view -> finish());
    }
}
