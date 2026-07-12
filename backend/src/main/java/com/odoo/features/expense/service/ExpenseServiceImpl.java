package com.odoo.features.expense.service;

import com.odoo.features.expense.entity.Expense;
import com.odoo.features.expense.dto.ExpenseRequestDTO;
import com.odoo.features.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    // private final TripRepository tripRepository; // TODO: Uncomment when Aryan creates it

    @Override
    public Expense logExpense(ExpenseRequestDTO dto) {
        Expense expense = new Expense();
        expense.setExpenseType(dto.getExpenseType());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());

        // Trip linking hum baad mein karenge
        // Trip trip = tripRepository.findById(dto.getTripId()).orElseThrow(...);
        // expense.setTrip(trip);

        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
}