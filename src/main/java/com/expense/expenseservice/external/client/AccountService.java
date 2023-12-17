package com.expense.expenseservice.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
