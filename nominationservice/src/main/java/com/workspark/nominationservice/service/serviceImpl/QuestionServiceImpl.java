package com.workspark.nominationservice.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.enums.CategoryType;
import com.workspark.nominationservice.enums.Constants;
import com.workspark.nominationservice.exception.QuestionNotFoundException;
import com.workspark.nominationservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.workspark.nominationservice.dto.QuestionDTO;
import com.workspark.nominationservice.model.Question;
import com.workspark.nominationservice.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public PaginatedRes<QuestionDTO> getAllMandatoryQuestions(CategoryType categoryType, int page, int size) {
        Page<Question> questionPage = questionRepository.findAllByType(categoryType, PageRequest.of(page, size));

        if (questionPage.isEmpty()) {
            throw new QuestionNotFoundException(Constants.QUESTION_NOT_FOUND.getMessage(categoryType));
        }

        return PaginatedRes.<QuestionDTO>builder()
                .data(questionPage.getContent().stream().map(this::mapToDTO).toList())
                .pageNo(page)
                .pageSize(size)
                .totalPages(questionPage.getTotalPages())
                .totalCount(questionPage.getTotalElements())
                .build();
    }

    public QuestionDTO mapToDTO(Question question) {
        return objectMapper.convertValue(question, QuestionDTO.class);
    }
}
