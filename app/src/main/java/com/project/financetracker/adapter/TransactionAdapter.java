package com.project.financetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financetracker.R;
import com.project.financetracker.model.TransactionModel;

import java.util.List;
import java.util.Locale;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<TransactionModel> transactionModels;

    public TransactionAdapter(List<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
    }

    public static class TransactionHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView amount;

        public TransactionHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.transaction_label);
            amount = itemView.findViewById(R.id.transaction_amount);
        }
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_layout, parent, false);
        return new TransactionHolder(view);
    }


    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        TransactionModel transactionModel = transactionModels.get(position);
        Context context = holder.amount.getContext();

        if (transactionModel.getAmount() >= 0) {
            holder.amount.setText(String.format(Locale.ENGLISH, "+ Rp%.0f", transactionModel.getAmount()));
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green1));
        } else {
            holder.amount.setText(String.format(Locale.ENGLISH, "- Rp%.0f", Math.abs(transactionModel.getAmount())));
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.orange1));
        }

        holder.label.setText(transactionModel.getLabel());
    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }

    public void setData(List<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
        notifyItemInserted(transactionModels.size());
    }
}
