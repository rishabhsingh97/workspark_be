package com.workspark.nominationservice.service;

import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.QuestionDTO;
import com.workspark.nominationservice.enums.CategoryType;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
   PaginatedRes<QuestionDTO> getAllMandatoryQuestions(CategoryType categoryType, int page, int size);
}
