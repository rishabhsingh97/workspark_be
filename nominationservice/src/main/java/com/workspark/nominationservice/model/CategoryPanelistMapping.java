package com.workspark.nominationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category_panelist_mapping")
public class CategoryPanelistMapping {

    @EmbeddedId
    private CategoryPanelistMappingId id;

    @ManyToOne
    @MapsId("categoryId")
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private RecognitionCategory recognitionCategory;



}
