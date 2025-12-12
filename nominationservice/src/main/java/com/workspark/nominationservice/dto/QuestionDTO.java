package com.workspark.nominationservice.dto;

import com.workspark.nominationservice.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for questions.
 * Contains validation annotations to ensure data integrity.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO implements Serializable {

    private Long questionId;


    private String question;

    private CategoryType type;

    private String dataType;
}
