package com.expense.expenseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long expenseId;

    @Column(name = "ACCOUNT_ID")
    private long accountId;

    @Column(name = "EXPENSE_DESCRIPTION")
    private String expenseDescription;

    @Column(name = "AMOUNT")
    private long amount;

    @Column(name = "EXPENSE_CATEGORY")
    private String expenseCategory;

    @Column(name = "EXPENSE_DATE")
    private Instant date;

    @Column(name = "EXPENSE_PERIOD")
    private String period;
}
