package com.expense.expenseservice.controller;

import com.expense.expenseservice.model.CategoryRequest;
import com.expense.expenseservice.model.CategoryResponse;
import com.expense.expenseservice.service.ExpenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense/category")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService categoryService;

    @PostMapping
    public ResponseEntity<Long> recordCategory(@RequestBody CategoryRequest categoryRequest) {
        long categoryId = categoryService.recordCategory(categoryRequest);
        return new ResponseEntity<>(categoryId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategory() {
        List<CategoryResponse> response = categoryService.listCategory();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyCategory(
            @PathVariable("id") long categoryId,
            @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.modifyCategory(categoryId, categoryRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCategory(
            @PathVariable("id") long categoryId
    ) {
        categoryService.removeCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
