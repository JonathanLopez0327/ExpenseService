package com.expense.expenseservice.external.client;

import com.expense.expenseservice.exception.ExpenseServiceCustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "ACCOUNT-SERVICE/account")
public interface AccountService {
    @PutMapping("/debitAmount/{id}")
    public ResponseEntity<Void> debitAmount(
            @PathVariable("id") long accountId,
            @RequestParam long amount
    );

    @PutMapping("/creditAmount/{id}")
    public ResponseEntity<Void> creditAmount(
            @PathVariable("id") long accountId,
            @RequestParam long amount
    );

    default void fallback(Exception e) {
        throw new ExpenseServiceCustomException(
                "Account Service is not available",
                "UNAVAILABLE",
                500
        );
    }
}
