package com.workspark.models.enitity;


import jakarta.persistence.*;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public class BaseAuditFields {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @PrePersist
    void intCreateFields() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        Instant now = Instant.now();
        createdAt = now;
        lastUpdatedAt = now;
    }

    @PreUpdate
    void intUpdateFields() {
        lastUpdatedAt = Instant.now();
    }

}
