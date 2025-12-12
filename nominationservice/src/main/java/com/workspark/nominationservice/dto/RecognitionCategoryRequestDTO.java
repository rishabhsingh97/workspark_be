package com.workspark.nominationservice.dto;

import com.workspark.nominationservice.enums.CategoryType;
import com.workspark.nominationservice.enums.SelectionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionCategoryRequestDTO {

    private Long categoryId;
    private String categoryName;
    private CategoryType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String cronExpression;
    private Boolean isActive;
    private Integer noOfDaysForNomination;
    private Integer noOfDaysForSelection;
    private Integer maxNomination;
    private List<Long> nominatedBy;
    private Boolean anonymousNomination;
    private SelectionType selectionType;
    private List<Long> panelistId;
    private Boolean certificateRequired;
    private Integer certificateTemplateId;
    private LocalDate certificateExpiryDate;
    private Integer maxWinner;
    private List<QuestionDTO> questions;

}
