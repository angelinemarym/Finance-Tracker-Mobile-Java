package com.project.financetracker.repository;

import com.project.financetracker.model.TransactionModel;

import java.text.ParseException;
import java.util.List;

public interface Repository {
    TransactionModel getById(int id) throws Exception;
    List<TransactionModel> getAll(int lastId, int limit) throws ParseException;
    boolean create(String label, double amount, String description);
    void update(int id, TransactionModel model);
    void delete(int id);
    double getBalance();
    double getIncome();
    double getExpense();
}
