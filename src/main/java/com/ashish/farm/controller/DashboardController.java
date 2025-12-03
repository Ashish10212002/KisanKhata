package com.ashish.farm.controller;

import com.ashish.farm.dto.DashboardSummaryDTO;
import com.ashish.farm.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard controller — returns totals scoped to the currently authenticated user.
 */
@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final TransactionService txService;

    @GetMapping("/api/dashboard/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        Double totalIncome = txService.totalByType("INCOME");
        Double totalExpense = txService.totalByType("EXPENSE");

        double income = totalIncome != null ? totalIncome : 0.0;
        double expense = totalExpense != null ? totalExpense : 0.0;
        double profit = income - expense;

        DashboardSummaryDTO dto = new DashboardSummaryDTO(income, expense, profit);
        return ResponseEntity.ok(dto);
    }
}
