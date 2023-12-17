package com.expense.expenseservice.service;

import com.expense.expenseservice.entity.Expense;
import com.expense.expenseservice.exception.ExpenseServiceCustomException;
import com.expense.expenseservice.external.client.AccountService;
import com.expense.expenseservice.model.ExpenseRequest;
import com.expense.expenseservice.model.ExpenseResponse;
import com.expense.expenseservice.repository.ExpenseRepository;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AccountService accountService;

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
    public long recordIncome(@Valid ExpenseRequest expenseRequest) {
        log.info("Recording new expense...");

        accountService.debitAmount(expenseRequest.getAccount(), expenseRequest.getAmount());
        log.info("Debiting amount of account {}", expenseRequest.getAccount());

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

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        log.info("Getting all expenses");

        List<Expense> expenseList = expenseRepository.findAll();

        return expenseList
                .stream()
                .map(expenseEntity-> {
                    ExpenseResponse expenseResponse = new ExpenseResponse();
                    copyProperties(expenseEntity, expenseResponse);
                    return expenseResponse;
                }).toList();
    }

    @Override
    public ExpenseResponse getExpenseById(long expenseId) {
        log.info("Get the expense with expenseId: {}", expenseId);

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseServiceCustomException("Expense with given id not found", "EXPENSE_NOT_FOUND"));

        ExpenseResponse expenseResponse = new ExpenseResponse();
        copyProperties(expense, expenseResponse);
        return expenseResponse;
    }

    @Override
    public void deleteExpenseById(long expenseId) {
        log.info("Get the expense for expenseId: {}", expenseId);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseServiceCustomException("Expense with given id not found", "EXPENSE_NOT_FOUND"));

        // When the user delete an expense, the expense must be credited
        log.info("Expense Removed of account: {}", expense.getAccountId());
        accountService.creditAmount(expense.getAccountId(), expense.getAmount());

        expenseRepository.delete(expense);
        log.info("Expense with id {} has been removed.", expenseId);
    }

    @Override
    public void setExpense(long expenseId, @Valid ExpenseRequest expenseRequest) {
        log.info("Get the expense for expenseId: {}", expenseId);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseServiceCustomException("Expense with given id not found", "EXPENSE_NOT_FOUND"));

        if (expenseRequest.getDescription() != null) {
            expense.setExpenseDescription(expenseRequest.getDescription());
        }

        if (expenseRequest.getCategory() != null) {
            expense.setExpenseCategory(expenseRequest.getCategory());
        }

        // When both will be modified
        if (expenseRequest.getAccount() != 0 && expenseRequest.getAmount() != 0) {
            log.info("Removing expense to account : {}", expense.getAccountId());
            accountService.creditAmount(expense.getAccountId(), expense.getAmount());

            try {
                log.info("Adding expense to account : {}", expenseRequest.getAccount());
                accountService.debitAmount(expenseRequest.getAccount(), expenseRequest.getAmount());

                // Update the transaction only after both operations were successful
                expense.setAccountId(expenseRequest.getAccount());
                expense.setAmount(expenseRequest.getAmount());
                expenseRepository.save(expense);
                log.info("Expense updated Successfully!");
            } catch (ExpenseServiceCustomException e) {
                log.error("Error modifying expense to account: {}", expenseRequest.getAccount(), e);

                // Roll back the transaction in case of exception and then throw a custom exception
                log.info("Removing expense from account: {}", expense.getAccountId());
                accountService.debitAmount(expense.getAccountId(), expense.getAmount());

                throw new ExpenseServiceCustomException(
                        "Account does not have sufficient balance",
                        "INSUFFICIENT_BALANCE"
                );
            }
        }
    }
}
