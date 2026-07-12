package com.odoo.features.expense.controller;

import com.odoo.common.response.ApiResponse; // Naya import
import com.odoo.features.expense.entity.Expense;
import com.odoo.features.expense.dto.ExpenseRequestDTO;
import com.odoo.features.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> logExpense(@RequestBody ExpenseRequestDTO request) {
        Expense savedExpense = expenseService.logExpense(request);
        return new ResponseEntity<>(
                ApiResponse.success(savedExpense, "Expense logged successfully!"),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(ApiResponse.success(expenses, "All expenses fetched successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequestDTO request) {
        Expense updatedExpense = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedExpense, "Expense updated successfully!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Expense deleted successfully!"));
    }
}
