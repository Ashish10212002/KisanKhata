package com.ashish.farm.repository;

import com.ashish.farm.model.Transaction;
import com.ashish.farm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFarmIdOrderByDateDesc(Long farmId);

    List<Transaction> findByUserOrderByDateDesc(User user);

    List<Transaction> findByUserAndFarmIdIsNullOrderByDateDesc(User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE UPPER(t.type) = UPPER(:type) AND t.user = :user")
    Double sumAmountByTypeAndUser(@Param("type") String type, @Param("user") User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.farmId = :farmId AND UPPER(t.type)=UPPER(:type) AND t.user = :user")
    Double sumAmountByFarmAndTypeAndUser(@Param("farmId") Long farmId, @Param("type") String type, @Param("user") User user);
}
