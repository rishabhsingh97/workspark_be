//package com.workspark.nominationservice.controller;
//
//import com.workspark.models.response.PaginatedRes;
//import com.workspark.nominationservice.dto.QuestionDTO;
//import com.workspark.nominationservice.enums.CategoryType;
//import com.workspark.nominationservice.service.QuestionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class QuestionControllerTest {
//
//    @Mock
//    private QuestionService questionService;
//
//    @InjectMocks
//    private QuestionController questionController;
//
//    private CategoryType categoryType;
//
//    @BeforeEach
//    void setUp() {
//        categoryType = CategoryType.SPOT;
//    }
//
//    @Test
//    void testGetAllQuestions_Success() {
//        // Prepare mock data
//        QuestionDTO questionDTO = QuestionDTO.builder()
//                .questionId(1L)
//                .question("What is your name?")
//                .type(CategoryType.SPOT)
//                .dataType("BOOLEAN")
//                .build();
//        List<QuestionDTO> mockQuestions = List.of(questionDTO);
//
//        when(questionService.getAllMandatoryQuestions(categoryType, 1, 10)).thenReturn(PaginatedRes<mockQuestions> sk);
//        ResponseEntity<List<QuestionDTO>> response = questionController.getAllQuestions(categoryType);
//
//        // Assertions
//        assertEquals(HttpStatus.OK, response.getStatusCode()); // Ensure the response status is 200 OK
//        assertNotNull(response.getBody()); // Ensure the response body is not null
//      //  assertEquals(1, response.getBody().size()); // Ensure the correct number of questions are returned
//        assertEquals("What is your name?", response.getBody().get(0).getQuestion()); // Verify the question text
//    }
//
//    @Test
//    void testGetAllQuestions_EmptyList() {
//        // Prepare mock data (empty list)
//        List<QuestionDTO> mockQuestions = List.of();
//
//        // Mock the service method to return the empty list
//        when(questionService.getAllManadatoryQuestions(categoryType)).thenReturn(mockQuestions);
//
//        // Call the controller method
//        ResponseEntity<List<QuestionDTO>> response = questionController.getAllQuestions(categoryType);
//
//        // Assertions
//        assertEquals(HttpStatus.OK, response.getStatusCode()); // Ensure the response status is 200 OK
//        assertNotNull(response.getBody()); // Ensure the response body is not null
//        assertTrue(response.getBody().isEmpty()); // Ensure the response body is an empty list
//    }
//
//    @Test
//    void testGetAllQuestions_CategoryNotFound() {
//        // Simulate that the service throws a QuestionNotFoundException when no questions are found
//        when(questionService.getAllManadatoryQuestions(categoryType))
//                .thenThrow(new RuntimeException("Questions not found"));
//
//        // Call the controller method and assert exception handling (can be handled via @ControllerAdvice if applicable)
//        try {
//            questionController.getAllQuestions(categoryType);
//            fail("Expected RuntimeException to be thrown");
//        } catch (RuntimeException e) {
//            assertEquals("Questions not found", e.getMessage());
//        }
//    }
//}
