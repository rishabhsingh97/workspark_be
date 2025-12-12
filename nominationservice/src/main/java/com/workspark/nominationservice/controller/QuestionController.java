package com.workspark.nominationservice.controller;

import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.QuestionDTO;
import com.workspark.nominationservice.enums.CategoryType;
import com.workspark.nominationservice.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing questions.
 * This controller provides endpoints to retrieve and manage questions based on different categories.
 */
@RestController
@RequestMapping("/api/v1/questions")
@Tag(name = "QUESTION API", description = "APIs to manage questions")
@Slf4j
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Retrieves all mandatory questions for a given category type.
     *
     * @param categoryType the category type for which questions are retrieved
     * @return a list of QuestionDTO containing the questions
     */
    @Operation(summary = "Get all mandatory questions", description = "Fetches all mandatory questions for a specific category type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid category type provided",
                    content = @Content)
    })
    @GetMapping("/{categoryType}")
    public ResponseEntity<BaseRes<PaginatedRes<QuestionDTO>>> getAllQuestions(
            @Parameter(description = "The category type for which questions are retrieved", required = true)
            @PathVariable CategoryType categoryType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching all mandatory questions for category type: {}", categoryType);
        PaginatedRes<QuestionDTO> questions = questionService.getAllMandatoryQuestions(categoryType, page, size);
        return BaseRes.success(questions, "", HttpStatus.OK);
    }
}
