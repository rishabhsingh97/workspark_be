//package com.workspark.nominationservice.controller;
//
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.workspark.nominationservice.dto.RecognitionCategoryRequestDTO;
//import com.workspark.nominationservice.dto.RecognitionCategoryResponseDTO;
//import com.workspark.nominationservice.service.RecognitionCategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class RecognitionCategoryControllerTest {
//
//    @Mock
//    private RecognitionCategoryService recognitionCategoryService;
//
//    @InjectMocks
//    private RecognitionCategoryController recognitionCategoryController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateRecognitionCategory_Success() throws JsonMappingException {
//        // Arrange
//        RecognitionCategoryRequestDTO requestDTO = new RecognitionCategoryRequestDTO();
//        requestDTO.setCategoryName("Employee of the Month");
//
//        RecognitionCategoryResponseDTO responseDTO = new RecognitionCategoryResponseDTO();
//        responseDTO.setCategoryId(1L);
//        responseDTO.setCategoryName("Employee of the Month");
//
//        when(recognitionCategoryService.createOrUpdateRecognitionCategory(requestDTO)).thenReturn(responseDTO);
//
//        // Act
//        ResponseEntity<RecognitionCategoryResponseDTO> response =
//                recognitionCategoryController.createRecognitionCategory(requestDTO);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//        verify(recognitionCategoryService, times(1)).createOrUpdateRecognitionCategory(requestDTO);
//    }
//
//    @Test
//    void testUpdateCategory_Success() throws JsonMappingException {
//        // Arrange
//        Long categoryId = 1L;
//        RecognitionCategoryRequestDTO requestDTO = new RecognitionCategoryRequestDTO();
//        requestDTO.setCategoryName("Updated Category Name");
//
//        RecognitionCategoryResponseDTO responseDTO = new RecognitionCategoryResponseDTO();
//        responseDTO.setCategoryId(categoryId);
//        responseDTO.setCategoryName("Updated Category Name");
//
//        when(recognitionCategoryService.createOrUpdateRecognitionCategory(requestDTO)).thenReturn(responseDTO);
//
//        // Act
//        ResponseEntity<RecognitionCategoryResponseDTO> response =
//                recognitionCategoryController.updateCategory( requestDTO);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//        verify(recognitionCategoryService, times(1)).createOrUpdateRecognitionCategory(requestDTO);
//    }
//
//    @Test
//    void testGetRecognitionCategory_Success() {
//        // Arrange
//        Long categoryId = 1L;
//
//        RecognitionCategoryResponseDTO responseDTO = new RecognitionCategoryResponseDTO();
//        responseDTO.setCategoryId(categoryId);
//        responseDTO.setCategoryName("Employee of the Month");
//
//        when(recognitionCategoryService.getRecognitionCategory(categoryId)).thenReturn(responseDTO);
//
//        // Act
//        ResponseEntity<RecognitionCategoryResponseDTO> response =
//                recognitionCategoryController.getRecognitionCategory(categoryId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseDTO, response.getBody());
//        verify(recognitionCategoryService, times(1)).getRecognitionCategory(categoryId);
//    }
//
//    @Test
//    void testGetRecognitionCategory_NotFound() {
//        // Arrange
//        Long categoryId = 999L;
//        when(recognitionCategoryService.getRecognitionCategory(categoryId))
//                .thenThrow(new RuntimeException("Category not found"));
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> recognitionCategoryController.getRecognitionCategory(categoryId));
//        assertEquals("Category not found", exception.getMessage());
//        verify(recognitionCategoryService, times(1)).getRecognitionCategory(categoryId);
//    }
//
//    @Test
//    void testGetAllRecognitionCategories_Success() {
//        // Arrange
//        RecognitionCategoryResponseDTO category1 = new RecognitionCategoryResponseDTO();
//        category1.setCategoryId(1L);
//        category1.setCategoryName("Employee of the Month");
//
//        RecognitionCategoryResponseDTO category2 = new RecognitionCategoryResponseDTO();
//        category2.setCategoryId(2L);
//        category2.setCategoryName("Best Innovator");
//
//        List<RecognitionCategoryResponseDTO> categories = List.of(category1, category2);
//
//        when(recognitionCategoryService.getAllRecognitionCategories()).thenReturn(categories);
//
//        // Act
//        ResponseEntity<List<RecognitionCategoryResponseDTO>> response =
//                recognitionCategoryController.getAllRecognitionCategories();
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(categories, response.getBody());
//        verify(recognitionCategoryService, times(1)).getAllRecognitionCategories();
//    }
//
//
//}
