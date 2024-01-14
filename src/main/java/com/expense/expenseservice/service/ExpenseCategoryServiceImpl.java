package com.expense.expenseservice.service;

import com.expense.expenseservice.entity.ExpenseCategory;
import com.expense.expenseservice.exception.ExpenseServiceCustomException;
import com.expense.expenseservice.model.CategoryRequest;
import com.expense.expenseservice.model.CategoryResponse;
import com.expense.expenseservice.repository.ExpenseCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository categoryRepository;

    @Override
    public long recordCategory(CategoryRequest categoryRequest) {
        log.info("Verifying Expense Category");
        ExpenseCategory expenseCategory = categoryRepository.findByName(categoryRequest.getName());

        if (expenseCategory == null) {
            log.info("Recording new Expense Category");
            ExpenseCategory category = ExpenseCategory.builder()
                    .name(categoryRequest.getName())
                    .build();
            categoryRepository.save(category);
            log.info("Expense Category Recorded Successfully!");
            return category.getCategoryId();
        }

        log.warn("Expense Category with name: {} Already exist", categoryRequest.getName());
        return 0;
    }

    @Override
    public List<CategoryResponse> listCategory() {
        log.info("Getting all Categories");
        List<ExpenseCategory> categories = categoryRepository.findAll();

        return categories
                .stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    BeanUtils.copyProperties(category, response);
                    return response;
                }).toList();
    }

    @Override
    public void modifyCategory(long categoryId, CategoryRequest categoryRequest) {
        log.info("Modifying Expense Category");

        ExpenseCategory category =
                categoryRepository.findById(categoryId).orElseThrow(
                        () -> new ExpenseServiceCustomException(
                                "Category with given Id not found",
                                "CATEGORY_NOT_FOUND",
                                404
                        ));

        if (!categoryRequest.getName().isEmpty()) {
            category.setName(categoryRequest.getName());

            ExpenseCategory expenseCategory = categoryRepository.findByName(categoryRequest.getName());

            if (expenseCategory == null) {
                categoryRepository.save(category);
                log.info("Expense Category Updated Successfully!");
            }
        }
    }

    @Override
    public void removeCategory(long categoryId) {
        log.info("Getting Expense Category with id : {}", categoryId);

        ExpenseCategory category =
                categoryRepository.findById(categoryId).orElseThrow(
                        () -> new ExpenseServiceCustomException(
                                "Category with given Id not found",
                                "CATEGORY_NOT_FOUND",
                                404
                        ));
        log.info("Removing category : {}", category.getName());
        categoryRepository.delete(category);
    }
}
