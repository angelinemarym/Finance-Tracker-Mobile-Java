package com.project.financetracker.repository;

import com.project.financetracker.model.TransactionModel;

import java.text.ParseException;
import java.sql.Date;
import java.util.List;

public interface ITransactionRepository {
    TransactionModel getById(int id) throws Exception;
    List<TransactionModel> getAll(int lastId, int limit) throws ParseException;
    List<TransactionModel> getByDateRange(Date start , Date end) throws ParseException;
    boolean create(String label, double amount, String description);
    void update(int id, TransactionModel model);
    void delete(int id);
    double getBalance();
    double getIncome();
    double getExpense();
    double getExpenseByDate(Date date);
}
