package com.odoo.features.expense.service;

import com.odoo.features.expense.entity.Expense;
import com.odoo.features.expense.dto.ExpenseRequestDTO;
import java.util.List;

public interface ExpenseService {
    Expense logExpense(ExpenseRequestDTO dto);
    List<Expense> getAllExpenses();
    Expense updateExpense(Long id, ExpenseRequestDTO dto);
    void deleteExpense(Long id);
}
