package com.expense.expenseservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponse {
    private long expenseId;
    private long accountId;
    private String expenseDescription;
    private long amount;
    private String expenseCategory;
    private String period;
}
