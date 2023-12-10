package com.expense.expenseservice.service;

import com.expense.expenseservice.model.ExpenseRequest;

public interface ExpenseService {
    long recordIncome(ExpenseRequest expenseRequest);
}
