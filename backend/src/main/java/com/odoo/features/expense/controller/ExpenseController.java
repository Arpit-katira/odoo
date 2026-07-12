package com.odoo.features.expense.controller;

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

    // POST API: Naya kharcha (expense) add karne ke liye
    @PostMapping
    public ResponseEntity<Expense> logExpense(@RequestBody ExpenseRequestDTO request) {
        Expense savedExpense = expenseService.logExpense(request);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    // GET API: Saare expenses dekhne ke liye
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
}