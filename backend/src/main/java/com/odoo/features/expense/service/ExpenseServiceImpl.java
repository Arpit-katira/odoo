package com.odoo.features.expense.service;

import com.odoo.features.expense.entity.Expense;
import com.odoo.features.expense.dto.ExpenseRequestDTO;
import com.odoo.features.expense.repository.ExpenseRepository;
import com.odoo.features.trip.repository.TripRepository; // Naya Import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.odoo.features.trip.entity.Trip;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository; // Ab yeh active hai

    @Override
    public Expense logExpense(ExpenseRequestDTO dto) {
        Expense expense = new Expense();
        expense.setExpenseType(dto.getExpenseType());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());

        // Linking Expense to Trip (Agar tripId request mein aayi hai)
        if (dto.getTripId() != null) {
            Trip trip = tripRepository.findById(dto.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found with ID: " + dto.getTripId()));
            expense.setTrip(trip);
        }

        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
}