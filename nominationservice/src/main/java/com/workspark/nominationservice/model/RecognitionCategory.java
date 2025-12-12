package com.workspark.nominationservice.model;

import com.workspark.nominationservice.enums.CategoryType;
import com.workspark.nominationservice.enums.SelectionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recognition_category")
public class RecognitionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CategoryType type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "cron_expression", length = 60)
    private String cronExpression;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name= "no_of_days_to_nomination")
    private Integer noOfDaysForNomination;

    @Column(name="no_of_days_to_selection")
    private Integer noOfDaysForSelection;

    @Column(name = "max_nomination")
    private Integer maxNomination;

    @Column(name = "anonymous_nomination")
    private Boolean anonymousNomination;

    @Enumerated(EnumType.STRING)
    @Column(name = "selection_type")
    private SelectionType selectionType;

    @Column(name = "certificate_required")
    private Boolean certificateRequired;

    @Column(name = "certificate_template_id")
    private Integer certificateTemplateId;

    @Column(name = "certificate_expiry_date")
    private LocalDate certificateExpiryDate;

    @Column(name = "max_winner")
    private Integer maxWinner;

    @CreationTimestamp
    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_date_time")
    private LocalDateTime updatedDateTime = LocalDateTime.now();

    @OneToMany(mappedBy = "recognitionCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionCategoryMapping> questionCategoryMappings;

    @OneToMany(mappedBy = "recognitionCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryNominatorsMapping> categoryNominatorsMappings;

    @OneToMany(mappedBy = "recognitionCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryPanelistMapping> categoryPanelistMappings;

}
