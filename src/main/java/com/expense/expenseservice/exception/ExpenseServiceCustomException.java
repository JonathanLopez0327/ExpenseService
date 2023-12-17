package com.expense.expenseservice.exception;

import lombok.Data;

@Data
public class ExpenseServiceCustomException extends RuntimeException {
    private String errorCode;
    private int status;

    public ExpenseServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ExpenseServiceCustomException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
