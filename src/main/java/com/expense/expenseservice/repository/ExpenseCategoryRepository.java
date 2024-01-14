package com.expense.expenseservice.repository;

import com.expense.expenseservice.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    ExpenseCategory findByName(String name);
}
