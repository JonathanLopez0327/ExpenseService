package com.expense.expenseservice.service;

import com.expense.expenseservice.entity.Expense;
import com.expense.expenseservice.model.ExpenseRequest;
import com.expense.expenseservice.repository.ExpenseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Log4j2
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    private String getCurrentPeriod() {
        String period = "";
        try {
            Instant currentInstant = Instant.now();
            YearMonth yearMonth = YearMonth.from(currentInstant.atZone(ZoneId.systemDefault()));

            // Formatear el YearMonth como "yyyyMM"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            period = yearMonth.format(formatter);
        } catch (Exception var3) {
            log.error("Error getting current period", var3);
        }

        return period;
    }

    @Override
    public long recordIncome(ExpenseRequest expenseRequest) {
        log.info("Recording new expense...");

        Expense expense = Expense.builder()
                .accountId(expenseRequest.getAccount())
                .expenseDescription(expenseRequest.getDescription())
                .amount(expenseRequest.getAmount())
                .expenseCategory(expenseRequest.getCategory())
                .date(Instant.now())
                .period(getCurrentPeriod())
                .build();

        expenseRepository.save(expense);
        log.info("Expense Recorder");

        return expense.getExpenseId();
    }
}
