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
import com.project.financetracker.RecyclerViewInterface;
import com.project.financetracker.model.TransactionModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<TransactionModel> transactionModels;
    private final RecyclerViewInterface recyclerViewInterface;

    public TransactionAdapter(List<TransactionModel> transactionModels, RecyclerViewInterface recyclerViewInterface) {
        this.transactionModels = transactionModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public static class TransactionHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView amount;
        public TextView description;
        public TextView createdAt;

        public TransactionHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            label = itemView.findViewById(R.id.transaction_label);
            amount = itemView.findViewById(R.id.transaction_amount);
            description = itemView.findViewById(R.id.transaction_description);
            createdAt = itemView.findViewById(R.id.transaction_createdAt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_layout, parent, false);
        return new TransactionHolder(view, recyclerViewInterface);
    }


    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        TransactionModel transactionModel = transactionModels.get(position);
        Context context = holder.amount.getContext();

        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        String amountStr = nf.format(transactionModel.getAmount());
        String amountStr2 = nf.format(Math.abs(transactionModel.getAmount()));

        if (transactionModel.getAmount() >= 0) {
            holder.amount.setText("Rp" + amountStr);
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green1));
        } else {
            holder.amount.setText("Rp" + amountStr2);
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.orange1));
        }

        holder.label.setText(transactionModel.getLabel());
        holder.description.setText(transactionModel.getDescription());
        holder.createdAt.setText(transactionModel.getCreatedAt().toString());
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
