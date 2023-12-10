package com.expense.expenseservice.controller;

import com.expense.expenseservice.model.ExpenseRequest;
import com.expense.expenseservice.service.ExpenseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expense")
@Log4j2
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Long> addExpense(@RequestBody ExpenseRequest expenseRequest) {
        long expenseId = expenseService.recordIncome(expenseRequest);
        return new ResponseEntity<>(expenseId, HttpStatus.CREATED);
    }
}
