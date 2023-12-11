package com.expense.expenseservice.service;

import com.expense.expenseservice.model.ExpenseRequest;
import com.expense.expenseservice.model.ExpenseResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ExpenseService {
    long recordIncome(@Valid ExpenseRequest expenseRequest);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpenseById(long expenseId);

    void deleteExpenseById(long expenseId);

    void setExpense(long expenseId, @Valid ExpenseRequest expenseRequest);
}
