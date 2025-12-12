package com.workspark.nominationservice.model;



import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
//@Table(name = "recognition_tier")
@Data 
//@NoArgsConstructor 
public class RecognitionTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tierId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime updatedDateTime;
}