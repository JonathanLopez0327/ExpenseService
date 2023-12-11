package com.expense.expenseservice.exception;

import lombok.Data;

@Data
public class ExpenseServiceCustomException extends RuntimeException {
    private String errorCode;

    public ExpenseServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
