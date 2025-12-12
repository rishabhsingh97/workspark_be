package com.workspark.nominationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryNominatorMappingId implements Serializable {

    @Column(name = "nominated_by", nullable = false)
    private Long nominatedBy;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;


}
