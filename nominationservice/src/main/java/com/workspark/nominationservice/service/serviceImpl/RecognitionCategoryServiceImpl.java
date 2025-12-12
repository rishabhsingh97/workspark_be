package com.workspark.nominationservice.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.models.pojo.AuthUser;
import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.QuestionDTO;
import com.workspark.nominationservice.enums.DataType;
import com.workspark.nominationservice.dto.RecognitionCategoryRequestDTO;
import com.workspark.nominationservice.dto.RecognitionCategoryResponseDTO;
import com.workspark.nominationservice.enums.Constants;
import com.workspark.nominationservice.exception.NoRecordsFoundException;
import com.workspark.nominationservice.exception.RecognitionCategoryNotFoundException;
import com.workspark.nominationservice.model.*;
import com.workspark.nominationservice.model.*;
import com.workspark.nominationservice.repository.CategoryNominatorsMappingRepository;
import com.workspark.nominationservice.repository.RecognitionCategoryRepository;
import com.workspark.nominationservice.service.RecognitionCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class RecognitionCategoryServiceImpl implements RecognitionCategoryService {

    private final RecognitionCategoryRepository recognitionCategoryRepository;
    private final CategoryNominatorsMappingRepository categoryNominatorsMappingRepository;
    private final ObjectMapper objectMapper;

    /**
     * Creates or updates a Recognition Category based on the provided DTO.
     *
     * @param dto The data transfer object containing category details.
     * @return The response DTO with the saved category details.
     * @throws RecognitionCategoryNotFoundException If the category with the given ID does not exist.
     */
    @Override
    public RecognitionCategoryResponseDTO createOrUpdateRecognitionCategory(RecognitionCategoryRequestDTO dto) {
        RecognitionCategory category;

        if (dto.getCategoryId() == null) {
            // Creating a new category
            log.info("Creating a new RecognitionCategory with details: {}", dto);
            category = new RecognitionCategory();
        } else {
            // Updating an existing category
            category = recognitionCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RecognitionCategoryNotFoundException(
                            Constants.CATEGORY_NOT_FOUND.getMessage(dto.getCategoryId())));


            // Clear mappings for update
            clearExistingMappings(category);
        }

        // Map fields from DTO to entity
        mapDtoToEntity(dto, category);

        // Handle mappings
        handleQuestionMappings(dto, category);
        handleNominatorMappings(dto, category);
        handlePanelistMappings(dto, category);

        // Save the category
        RecognitionCategory savedCategory = recognitionCategoryRepository.save(category);
        log.info("RecognitionCategory saved successfully with ID: {}", savedCategory.getCategoryId());

        // Prepare the response DTO
        RecognitionCategoryResponseDTO responseDTO = mapToResponseDTO(savedCategory);

        return responseDTO;
    }

    /**
     * Clears existing mappings related to a Recognition Category.
     * This method initializes the collections to avoid null pointer exceptions
     * and removes any existing mappings before updating.
     *
     * @param category The category whose mappings need to be cleared.
     */
    protected void clearExistingMappings(RecognitionCategory category) {

        // Initialize mutable collections
        if (category.getQuestionCategoryMappings() == null) {
            category.setQuestionCategoryMappings(new ArrayList<>());
        } else {
            category.getQuestionCategoryMappings().clear();
        }

        if (category.getCategoryNominatorsMappings() == null) {
            category.setCategoryNominatorsMappings(new ArrayList<>());
        } else {
            category.getCategoryNominatorsMappings().clear();
        }

        if (category.getCategoryPanelistMappings() == null) {
            category.setCategoryPanelistMappings(new ArrayList<>());
        } else {
            category.getCategoryPanelistMappings().clear();
        }
    }

    /**
     * Maps the fields from the DTO to the corresponding Recognition Category entity.
     *
     * @param dto      The Recognition Category request DTO.
     * @param category The Recognition Category entity to be updated.
     */

    protected void mapDtoToEntity(RecognitionCategoryRequestDTO dto, RecognitionCategory category) {
        try {
            category.setCategoryName(dto.getCategoryName());
            objectMapper.updateValue(category, dto);
        } catch (JsonMappingException e) {
            log.error("Error mapping DTO to entity: {}", e.getMessage());
            throw new RuntimeException("Mapping error", e);
        }
    }

    /**
     * Handles the mapping of questions from the DTO to the category's question-category mapping.
     *
     * @param dto      The Recognition Category request DTO.
     * @param category The Recognition Category entity to update.
     */
    private void handleQuestionMappings(RecognitionCategoryRequestDTO dto, RecognitionCategory category) {
        if (category.getQuestionCategoryMappings() == null) {
            category.setQuestionCategoryMappings(new ArrayList<>());
        }
        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            // Initialize the list if it's null


            List<QuestionCategoryMapping> updatedMappings = dto.getQuestions().stream()
                    .map(questionDto -> {
                        // Use ObjectMapper to map the QuestionDTO to QuestionCategoryMapping
                        QuestionCategoryMapping mapping = objectMapper.convertValue(questionDto, QuestionCategoryMapping.class);
                        mapping.setRecognitionCategory(category);  // Set the category reference
                        mapping.setDataType(DataType.valueOf(questionDto.getDataType()));  // Set the DataType enum
                        return mapping;
                    })
                    .collect(Collectors.toList());


            category.getQuestionCategoryMappings().addAll(updatedMappings);


        }
    }

    /**
     * Handles the mapping of nominators from the DTO to the category's nominator mapping.
     *
     * @param dto      The Recognition Category request DTO.
     * @param category The Recognition Category entity to update.
     */
    private void handleNominatorMappings(RecognitionCategoryRequestDTO dto, RecognitionCategory category) {
        if (dto.getNominatedBy() != null) {
            // Initialize the list if it's null
            if (category.getCategoryNominatorsMappings() == null) {
                category.setCategoryNominatorsMappings(new ArrayList<>());
            }

            List<CategoryNominatorsMapping> mappings = dto.getNominatedBy().stream().map(nominatedBy -> {
                CategoryNominatorsMapping mapping = new CategoryNominatorsMapping();
                CategoryNominatorMappingId mappingId = new CategoryNominatorMappingId(nominatedBy, category.getCategoryId());
                mapping.setId(mappingId);
                mapping.setRecognitionCategory(category);
                return mapping;
            }).toList();

            category.getCategoryNominatorsMappings().addAll(mappings);
        }

    }

    /**
     * Handles the mapping of panelists from the DTO to the category's panelist mapping.
     *
     * @param dto      The Recognition Category request DTO.
     * @param category The Recognition Category entity to update.
     */
    private void handlePanelistMappings(RecognitionCategoryRequestDTO dto, RecognitionCategory category) {
        if (dto.getPanelistId() != null) {
            log.info("Updating user panelist mappings for RecognitionCategory ID: {}", category.getCategoryId());

            // Initialize the list if it's null
            if (category.getCategoryPanelistMappings() == null) {
                category.setCategoryPanelistMappings(new ArrayList<>());
            }
            List<CategoryPanelistMapping> mappings = dto.getPanelistId().stream().map(panelistId -> {
                CategoryPanelistMapping mapping = new CategoryPanelistMapping();
                CategoryPanelistMappingId mappingId = new CategoryPanelistMappingId(panelistId, category.getCategoryId());
                mapping.setId(mappingId);
                mapping.setRecognitionCategory(category);
                return mapping;
            }).toList();

            category.getCategoryPanelistMappings().addAll(mappings);
        }
    }

    /**
     * Maps a Recognition Category entity to its corresponding response DTO.
     *
     * @param category The Recognition Category entity to be mapped.
     * @return The mapped Recognition Category Response DTO.
     */

    protected RecognitionCategoryResponseDTO mapToResponseDTO(RecognitionCategory category) {
        // Map basic fields using ObjectMapper
        RecognitionCategoryResponseDTO responseDTO = objectMapper.convertValue(category, RecognitionCategoryResponseDTO.class);

        // Manually handle custom fields
        List<Long> userIds = category.getCategoryNominatorsMappings().stream()
                .map(mapping -> mapping.getId().getNominatedBy())
                .collect(Collectors.toList());
        List<Long> panelistIds = category.getCategoryPanelistMappings().stream()
                .map(mapping -> mapping.getId().getPanelistId())
                .collect(Collectors.toList());

        List<QuestionDTO> questionDTOList = category.getQuestionCategoryMappings().stream()
                .map(this::mapToQuestionCategoryMappingResponseDTO)
                .collect(Collectors.toList());

        // Set the custom fields
        responseDTO.setNominatedBy(userIds);
        responseDTO.setPanelistId(panelistIds);
        responseDTO.setQuestions(questionDTOList);

        return responseDTO;
    }

    /**
     * Converts a QuestionCategoryMapping object to a QuestionDTO object.
     * <p>
     * This method takes a QuestionCategoryMapping, extracts relevant details such as
     * question ID, question text, and data type, and returns a QuestionDTO representing
     * the same information. The data type is converted to its string representation.
     *
     * @param mapping The QuestionCategoryMapping object to be converted.
     * @return A QuestionDTO object containing the extracted details from the given mapping.
     */
    protected QuestionDTO mapToQuestionCategoryMappingResponseDTO(QuestionCategoryMapping mapping) {
        return objectMapper.convertValue(mapping, QuestionDTO.class);

    }


    /**
     * Fetches a Recognition Category by its ID and returns the corresponding response DTO.
     *
     * @param categoryId The ID of the Recognition Category to fetch.
     * @return The Recognition Category Response DTO.
     * @throws RecognitionCategoryNotFoundException If the category with the given ID is not found.
     */
    @Override
    public RecognitionCategoryResponseDTO getRecognitionCategory(Long categoryId) {
        log.info("Fetching RecognitionCategory with ID: {}", categoryId);
        RecognitionCategory category = recognitionCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecognitionCategoryNotFoundException(
                        Constants.CATEGORY_NOT_FOUND.getMessage(categoryId)));


        RecognitionCategoryResponseDTO responseDTO = mapToResponseDTO(category);


        return responseDTO;
    }

    /**
     * Fetches all Recognition Categories and returns them as a list of response DTOs.
     *
     * @return A list of all Recognition Category Response DTOs.
     * @throws NoRecordsFoundException If no recognition categories are found.
     */
    @Override
    public PaginatedRes<RecognitionCategoryResponseDTO> getAllRecognitionCategories(int page, int size) {

        Page<RecognitionCategory> categoryPage = recognitionCategoryRepository.findAll(PageRequest.of(page, size));
        if (categoryPage.isEmpty()) {
            throw new NoRecordsFoundException("No recognition categories found.");
        }

        return PaginatedRes.<RecognitionCategoryResponseDTO>builder()
                .data(categoryPage.getContent().stream().map(this::mapToResponseDTO).toList())
                .pageNo(page)
                .pageSize(size)
                .totalPages(categoryPage.getTotalPages())
                .totalCount(categoryPage.getTotalElements())
                .build();
    }

    @Override
    public PaginatedRes<RecognitionCategoryResponseDTO> getAllRecognitionCategoriesByAuthUser(int page, int size) {

        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<CategoryNominatorsMapping> categoryPage = categoryNominatorsMappingRepository.findByIdNominatedBy(user.getId(), PageRequest.of(page, size));

        if (categoryPage.isEmpty()) {
            throw new NoRecordsFoundException("No recognition categories found.");
        }

        return PaginatedRes.<RecognitionCategoryResponseDTO>builder()
                .data(categoryPage.getContent().stream()
                        .map(CategoryNominatorsMapping::getRecognitionCategory)
                        .map(this::mapToResponseDTO).toList())
                .pageNo(page)
                .pageSize(size)
                .totalPages(categoryPage.getTotalPages())
                .totalCount(categoryPage.getTotalElements())
                .build();
    }

}

