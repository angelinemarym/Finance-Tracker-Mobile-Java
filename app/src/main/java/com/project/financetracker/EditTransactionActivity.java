package com.project.financetracker;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.financetracker.model.TransactionModel;
import com.project.financetracker.repository.Repository;
import com.project.financetracker.repository.TransactionRepository;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class EditTransactionActivity extends AppCompatActivity{

    TransactionRepository databaseHelper;
    TransactionModel model;
    private String selectedLabel;
    private Double selectedAmount;
    private String selectedDescription;
    private int selectedId;
    private Date selectedCreatedDate;
    private TextInputEditText labelInput;
    private TextInputEditText amountInput;
    private TextInputEditText descriptionInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        selectedId = getIntent().getIntExtra("id", -1);
        selectedLabel = getIntent().getStringExtra("label");
        selectedAmount = getIntent().getDoubleExtra("amount", -1);
        selectedDescription = getIntent().getStringExtra("description");
        selectedCreatedDate = (Date)getIntent().getSerializableExtra("createdAt");

        databaseHelper = new TransactionRepository(this);

        labelInput = (TextInputEditText) findViewById(R.id.labelInput);
        amountInput = (TextInputEditText) findViewById(R.id.amountInput);
        descriptionInput = (TextInputEditText) findViewById(R.id.descriptionInput);
        TextInputLayout labelLayout = (TextInputLayout) findViewById(R.id.labelLayout);
        TextInputLayout amountLayout = (TextInputLayout) findViewById(R.id.amountLayout);
        TextInputLayout descriptionLayout = (TextInputLayout) findViewById(R.id.descriptionLayout);

        Button addTransactionBtn = (Button) findViewById(R.id.addTransactionButton);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioButtonExpense = (RadioButton) findViewById(R.id.radioButtonExpense);
        RadioButton radioButtonIncome = (RadioButton) findViewById(R.id.radioButtonIncome);

        if(selectedAmount < 0){
            radioButtonExpense.setChecked(true);
        }
        else if(selectedAmount > 0){
            radioButtonIncome.setChecked(true);
        }
        else {
            radioGroup.clearCheck();
        }

        setText();

        addTransactionBtn.setOnClickListener(view -> {
            double amount = Double.parseDouble(amountInput.getText().toString().isEmpty() ? "0" : amountInput.getText().toString());
            String label = labelInput.getText().toString();
            String description = descriptionInput.getText().toString();

            if (label.isEmpty())
                labelLayout.setError("Please enter a valid label");
            if (amount == 0)
                amountLayout.setError("Please enter a valid amount");

            if (radioButtonExpense.isChecked()) {
                model = new TransactionModel(selectedId, label, amount * -1, description, selectedCreatedDate);
            } else if (radioButtonIncome.isChecked()) {
                model = new TransactionModel(selectedId, label, amount, description, selectedCreatedDate);
            }

            databaseHelper.update(selectedId, model);
            finish();
        });
        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(view -> finish());
    }

    public void setText() {
        labelInput.setText(selectedLabel);
        if (selectedAmount < 0) {selectedAmount*=-1;}
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        String selectedAmountStr = nf.format(selectedAmount);
        amountInput.setText(String.format(Locale.ENGLISH, "%.0f", selectedAmount));
        descriptionInput.setText(selectedDescription);
    }
}