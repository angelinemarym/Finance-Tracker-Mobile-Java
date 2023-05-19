package com.project.financetracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class TransactionModel implements Parcelable, Serializable {
    private int id;
    private final String label;
    private final double amount;
    private String description;
    private Date createdAt;

    public TransactionModel(int id, String label, double amount, String description, Date createdAt){
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }


    protected TransactionModel(Parcel in) {
        label = in.readString();
        amount = in.readDouble();
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    public String getLabel(){
        return label;
    }

    public double getAmount(){
        return amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeDouble(amount);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
