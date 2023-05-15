package com.project.financetracker;

public class Transaction {
    private String label;
    private double amount;

    Transaction(String label, double amount){
        this.label = label;
        this.amount = amount;
    }

    public String getLabel(){
        return label;
    }

    public double getAmount(){
        return amount;
    }
}
