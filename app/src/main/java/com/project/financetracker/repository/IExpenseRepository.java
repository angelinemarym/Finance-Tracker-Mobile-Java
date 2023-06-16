package com.project.financetracker.repository;

public interface IExpenseRepository {
    double setExpenseLimit(double expenseLimit);
    double getExpenseLimit() throws Exception;
}
