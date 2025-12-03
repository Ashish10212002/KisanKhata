package com.ashish.farm.service;

import com.ashish.farm.model.Transaction;
import com.ashish.farm.model.User;
import com.ashish.farm.model.Farm;
import com.ashish.farm.repository.TransactionRepository;
import com.ashish.farm.repository.UserRepository;
import com.ashish.farm.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository txRepo;
    private final UserRepository userRepository;
    private final FarmRepository farmRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Transaction createTransaction(Transaction tx) {
        User current = getCurrentUser();
        if (tx.getFarmId() != null) {
            Farm farm = farmRepository.findById(tx.getFarmId())
                    .orElseThrow(() -> new RuntimeException("Farm not found"));
            if (farm.getUser() == null || !farm.getUser().getId().equals(current.getId())) {
                throw new RuntimeException("Cannot create transaction for a farm you don't own");
            }
        }
        tx.setUser(current);
        return txRepo.save(tx);
    }

    public Optional<Transaction> getById(Long id) {
        return txRepo.findById(id);
    }

    public List<Transaction> getByFarmId(Long farmId) {
        User current = getCurrentUser();
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new RuntimeException("Farm not found"));
        if (farm.getUser() == null || !farm.getUser().getId().equals(current.getId())) {
            throw new RuntimeException("Not authorized to view transactions for this farm");
        }
        return txRepo.findByFarmIdOrderByDateDesc(farmId);
    }

    public List<Transaction> getAllTransactionsDesc() {
        User current = getCurrentUser();
        return txRepo.findByUserOrderByDateDesc(current);
    }

    public List<Transaction> getCommonTransactionsDesc() {
        User current = getCurrentUser();
        return txRepo.findByUserAndFarmIdIsNullOrderByDateDesc(current);
    }

    public Double totalByType(String type) {
        User current = getCurrentUser();
        return txRepo.sumAmountByTypeAndUser(type, current);
    }

    public Double totalByFarmAndType(Long farmId, String type) {
        User current = getCurrentUser();
        return txRepo.sumAmountByFarmAndTypeAndUser(farmId, type, current);
    }

    public Transaction updateTransaction(Long id, Transaction details) {
        User current = getCurrentUser();
        Transaction tx = txRepo.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (tx.getUser() == null || !tx.getUser().getId().equals(current.getId())) {
            throw new RuntimeException("Not authorized to edit this transaction");
        }

        if (details.getFarmId() != null) {
            Farm farm = farmRepository.findById(details.getFarmId())
                    .orElseThrow(() -> new RuntimeException("Farm not found"));
            if (farm.getUser() == null || !farm.getUser().getId().equals(current.getId())) {
                throw new RuntimeException("Cannot assign transaction to a farm you don't own");
            }
        }

        tx.setAmount(details.getAmount());
        tx.setCategory(details.getCategory());
        tx.setDate(details.getDate());
        tx.setDescription(details.getDescription());
        tx.setType(details.getType());
        tx.setFarmId(details.getFarmId());
        tx.setQuantity(details.getQuantity());
        tx.setUnit(details.getUnit());

        return txRepo.save(tx);
    }
}
