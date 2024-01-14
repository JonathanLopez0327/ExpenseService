package com.expense.expenseservice.service;

import com.expense.expenseservice.model.CategoryRequest;
import com.expense.expenseservice.model.CategoryResponse;

import java.util.List;

public interface ExpenseCategoryService {
    long recordCategory(CategoryRequest name);

    List<CategoryResponse> listCategory();

    void modifyCategory(long categoryId, CategoryRequest categoryRequest);

    void removeCategory(long categoryId);
}
