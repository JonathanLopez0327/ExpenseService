package com.expense.expenseservice.controller;

import com.expense.expenseservice.model.ExpenseRequest;
import com.expense.expenseservice.model.ExpenseResponse;
import com.expense.expenseservice.service.ExpenseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @RolesAllowed({"B_Administrator","B_Regular_Customer"})
    @PostMapping
    public ResponseEntity<Long> addExpense(@RequestBody @Valid ExpenseRequest expenseRequest) {
        long expenseId = expenseService.recordIncome(expenseRequest);
        return new ResponseEntity<>(expenseId, HttpStatus.CREATED);
    }

    @RolesAllowed({"B_Administrator","B_Regular_Customer", "B_Auditor"})
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> expenseResponse = expenseService.getAllExpenses();
        return new ResponseEntity<>(expenseResponse, HttpStatus.OK);
    }

    @RolesAllowed({"B_Administrator","B_Regular_Customer", "B_Auditor"})
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable("id") long expenseId) {
        ExpenseResponse expenseResponse
                = expenseService.getExpenseById(expenseId);
        return new ResponseEntity<>(expenseResponse, HttpStatus.OK);
    }

    @RolesAllowed({"B_Administrator","B_Regular_Customer"})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpense(@PathVariable("id") long expenseId,
                                              @RequestBody @Valid ExpenseRequest expenseRequest) {
        expenseService.setExpense(expenseId, expenseRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RolesAllowed({"B_Administrator","B_Regular_Customer"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable("id") long expenseId) {
        expenseService.deleteExpenseById(expenseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
