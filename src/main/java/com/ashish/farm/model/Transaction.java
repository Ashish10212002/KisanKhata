package com.ashish.farm.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "farm_id", nullable = true)
    private Long farmId;

    private LocalDate date;
    private String type;     // EXPENSE or INCOME
    private String category;
    private Double amount;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double quantity;
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
