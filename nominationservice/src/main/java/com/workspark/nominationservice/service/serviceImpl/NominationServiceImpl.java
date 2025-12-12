package com.workspark.nominationservice.service.serviceImpl;

import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.NominationRequestDTO;
import com.workspark.nominationservice.dto.NominationResponseDTO;
import com.workspark.nominationservice.dto.QuestionAnswerDTO;
import com.workspark.nominationservice.dto.RecognitionCategoryResponseDTO;
import com.workspark.nominationservice.exception.NominationNotFoundException;
import com.workspark.nominationservice.exception.RecognitionCategoryNotFoundException;
import com.workspark.nominationservice.model.Nomination;
import com.workspark.nominationservice.model.NominationQuestionAnswerMapping;
import com.workspark.nominationservice.repository.NominationQuestionAnswerMappingRepository;
import com.workspark.nominationservice.repository.NominationRepository;

import com.workspark.nominationservice.service.NominationService;
import com.workspark.nominationservice.service.RecognitionCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NominationServiceImpl implements NominationService {

    // Repositories and services required for the operations
    private final NominationRepository nominationRepository;
    private final NominationQuestionAnswerMappingRepository mappingRepository;
    private final RecognitionCategoryService recognitionCategoryService;

    /**
     * Helper method to convert a Nomination entity into a NominationResponseDTO.
     * 
     * @param nomination the Nomination entity to be converted
     * @return the converted NominationResponseDTO
     */
    private NominationResponseDTO convertToResponseDTO(Nomination nomination) {
        List<QuestionAnswerDTO> questionAnswers = nomination.getQuestionAnswerMappings().stream()
                .map(mapping -> QuestionAnswerDTO.builder()
                        .questionId(mapping.getQuestionId())
                        .answer(mapping.getAnswer())
                        .build())
                .toList();

        return NominationResponseDTO.builder()
                .id(nomination.getId())
                .categoryId(nomination.getCategoryId())
                .nomineeName(nomination.getNomineeName())
                .nomineeEmployeeId(nomination.getNomineeEmployeeId())
                .nomineeEmail(nomination.getNomineeEmail())
                .nominatedByUserId(nomination.getNominatedByUserId())
                .questionAnswers(questionAnswers)
                .createdAt(nomination.getCreatedAt())
                .updatedAt(nomination.getUpdatedAt())
                .build();
    }

    /**
     * Create a new nomination based on the request DTO.
     * 
     * @param requestDTO the data transfer object containing nomination details
     * @return the created NominationResponseDTO
     */
    public NominationResponseDTO createNomination(NominationRequestDTO requestDTO) {
        // Validate the category ID
        RecognitionCategoryResponseDTO category = recognitionCategoryService.getRecognitionCategory(requestDTO.getCategoryId());
        if (category == null) {
            throw new RecognitionCategoryNotFoundException("Category with ID " + requestDTO.getCategoryId() + " not found");
        }

        // Build a new Nomination entity
        Nomination nomination = Nomination.builder()
                .categoryId(requestDTO.getCategoryId())
                .nomineeEmployeeId(requestDTO.getNomineeId())
                .build();

        // Map question-answer pairs
        List<NominationQuestionAnswerMapping> mappings = requestDTO.getQuestionAnswers().stream()
                .map(qa -> NominationQuestionAnswerMapping.builder()
                        .questionId(qa.getQuestionId())
                        .answer(qa.getAnswer())
                        .nomination(nomination)
                        .build())
                .toList();

        nomination.setQuestionAnswerMappings(mappings);
        Nomination savedNomination = nominationRepository.save(nomination);

        // Return the response DTO
        return convertToResponseDTO(savedNomination);
    }

    /**
     * Retrieve a nomination by its ID.
     * 
     * @param nominationId the ID of the nomination
     * @return the retrieved NominationResponseDTO
     */
    public NominationResponseDTO getNominationById(Long nominationId) {
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new NominationNotFoundException("Nomination with ID " + nominationId + " not found"));

        return convertToResponseDTO(nomination);
    }

    /**
     * Update an existing nomination based on its ID and the provided request DTO.
     * 
     * @param nominationId the ID of the nomination to be updated
     * @param requestDTO the updated data transfer object
     * @return the updated NominationResponseDTO
     */
    public NominationResponseDTO updateNomination(Long nominationId, NominationRequestDTO requestDTO) {
        // Fetch the existing nomination
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new NominationNotFoundException("Nomination with ID " + nominationId + " not found"));

        // Update nomination fields
        nomination.setCategoryId(requestDTO.getCategoryId());
        nomination.setNomineeEmployeeId(requestDTO.getNomineeId());
        nomination.setUpdatedAt(java.time.LocalDateTime.now());

        // Replace question-answer mappings
        List<NominationQuestionAnswerMapping> existingMappings = nomination.getQuestionAnswerMappings();
        existingMappings.clear();
        List<NominationQuestionAnswerMapping> newMappings = requestDTO.getQuestionAnswers().stream()
                .map(qa -> NominationQuestionAnswerMapping.builder()
                        .questionId(qa.getQuestionId())
                        .answer(qa.getAnswer())
                        .nomination(nomination)
                        .build())
                .toList();
        existingMappings.addAll(newMappings);

        Nomination updatedNomination = nominationRepository.save(nomination);

        return convertToResponseDTO(updatedNomination);
    }

    /**
     * Retrieve a paginated list of all nominations.
     * 
     * @param page the page number (zero-based)
     * @param size the number of records per page
     * @return a paginated list of NominationResponseDTO
     */
    public PaginatedRes<NominationResponseDTO> getAllNominations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Nomination> nominationPage = nominationRepository.findAll(pageable);

        return PaginatedRes.<NominationResponseDTO>builder()
                .data(nominationPage.getContent().stream().map(this::convertToResponseDTO).toList())
                .pageNo(page)
                .pageSize(size)
                .totalPages(nominationPage.getTotalPages())
                .totalCount(nominationPage.getTotalElements())
                .build();
    }
}
