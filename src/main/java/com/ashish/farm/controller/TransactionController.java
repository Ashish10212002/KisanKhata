package com.ashish.farm.controller;

import com.ashish.farm.model.Transaction;
import com.ashish.farm.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService txService;

    // Global history (only logged-in user)
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(txService.getAllTransactionsDesc());
    }

    // Farm-specific history (only if user owns the farm)
    @GetMapping("/farm/{farmId}")
    public ResponseEntity<List<Transaction>> getTransactionsForFarm(@PathVariable Long farmId) {
        return ResponseEntity.ok(txService.getByFarmId(farmId));
    }

    // Common transactions (farmId = null)
    @GetMapping("/common")
    public ResponseEntity<List<Transaction>> getCommonTransactions() {
        return ResponseEntity.ok(txService.getCommonTransactionsDesc());
    }

    // Create transaction (farm-specific or common)
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction tx) {
        Transaction created = txService.createTransaction(tx);
        return ResponseEntity.ok(created);
    }

    // Update transaction (only if user owns it)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody Transaction details) {
        Transaction updated = txService.updateTransaction(id, details);
        return ResponseEntity.ok(updated);
    }
}
