package com.expense.expenseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryId;

    @Column(name = "CATEGORY")
    private String name;
}
