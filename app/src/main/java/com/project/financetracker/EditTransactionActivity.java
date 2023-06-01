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

import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.financetracker.repository.Repository;
import com.project.financetracker.repository.TransactionRepository;

public class EditTransactionActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        TextInputEditText labelInput = (TextInputEditText) findViewById(R.id.labelInput);
        TextInputEditText amountInput = (TextInputEditText) findViewById(R.id.amountInput);
        TextInputEditText descriptionInput = (TextInputEditText) findViewById(R.id.descriptionInput);
        TextInputLayout labelLayout = (TextInputLayout) findViewById(R.id.labelLayout);
        TextInputLayout amountLayout = (TextInputLayout) findViewById(R.id.amountLayout);
        TextInputLayout descriptionLayout = (TextInputLayout) findViewById(R.id.descriptionLayout);

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

        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeButton);

        closeBtn.setOnClickListener(view -> finish());
    }
}