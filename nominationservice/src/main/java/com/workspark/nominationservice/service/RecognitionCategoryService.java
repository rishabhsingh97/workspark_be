package com.workspark.nominationservice.service;

import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.RecognitionCategoryRequestDTO;
import com.workspark.nominationservice.dto.RecognitionCategoryResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface RecognitionCategoryService {
    RecognitionCategoryResponseDTO createOrUpdateRecognitionCategory(RecognitionCategoryRequestDTO dto) ;
    RecognitionCategoryResponseDTO getRecognitionCategory(Long categoryId);
    PaginatedRes<RecognitionCategoryResponseDTO> getAllRecognitionCategories(int page, int size);
    PaginatedRes<RecognitionCategoryResponseDTO> getAllRecognitionCategoriesByAuthUser(int page, int size);
}
