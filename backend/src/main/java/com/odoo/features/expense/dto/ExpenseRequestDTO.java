package com.odoo.features.expense.dto;

import com.odoo.features.expense.entity.ExpenseType;
import lombok.Data;

@Data
public class ExpenseRequestDTO {
    private Long tripId; // Trip ko baad mein set karenge jab Aryan ka module ready hoga
    private ExpenseType expenseType;
    private Double amount;
    private String description;
}