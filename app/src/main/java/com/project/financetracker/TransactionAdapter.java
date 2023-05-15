package com.project.financetracker;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private ArrayList<Transaction> transactions;

    public TransactionAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
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

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_layout, parent, false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        Context context = holder.amount.getContext();

        if (transaction.getAmount() >= 0) {
            holder.amount.setText(String.format("+ Rp%.0f", transaction.getAmount()));
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green1));
        } else {
            holder.amount.setText(String.format("- Rp%.0f", Math.abs(transaction.getAmount())));
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.orange1));
        }

        holder.label.setText(transaction.getLabel());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailedActivity.class);
//                intent.putExtra("transaction", transaction.getLabel());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setData(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }
}
