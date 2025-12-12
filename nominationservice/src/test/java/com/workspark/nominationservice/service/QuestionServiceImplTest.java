//package com.workspark.nominationservice.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.workspark.nominationservice.dto.QuestionDTO;
//import com.workspark.nominationservice.enums.CategoryType;
//import com.workspark.nominationservice.enums.DataType;
//import com.workspark.nominationservice.exception.QuestionNotFoundException;
//import com.workspark.nominationservice.model.Question;
//import com.workspark.nominationservice.service.serviceImpl.QuestionServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.workspark.nominationservice.repository.QuestionRepository;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class QuestionServiceImplTest {
//
//
//    @Mock
//    private QuestionRepository questionRepository;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @InjectMocks
//    private QuestionServiceImpl questionService;
//
//    @Test
//    void testGetAllManadatoryQuestions_withQuestions() {
//        // Arrange
//        CategoryType categoryType = CategoryType.SPOT;
//        Question question1 = new Question(1L, "What is your name?", categoryType,DataType.BOOLEAN);
//        Question question2 = new Question(2L, "What is your age?", categoryType, DataType.BOOLEAN);
//
//      //  QuestionDTO questionDTO1 = new QuestionDTO(1L, "What is your name?", categoryType, DataType.BOOLEAN);
//      //  QuestionDTO questionDTO2 = new QuestionDTO(2L, "What is your age?", categoryType, DataType.BOOLEAN);
//        QuestionDTO questionDTO1 = QuestionDTO.builder()
//                .questionId(1L)
//                .question("What is your name?")
//                .type(CategoryType.SPOT)
//                .dataType("BOOLEAN")
//                .build();
//        QuestionDTO questionDTO2 = QuestionDTO.builder()
//                .questionId(1L)
//                .question("What is your age?")
//                .type(CategoryType.NOMINATION)
//                .dataType("BOOLEAN")
//                .build();
//        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));
//        when(objectMapper.convertValue(question1, QuestionDTO.class)).thenReturn(questionDTO1);
//        when(objectMapper.convertValue(question2, QuestionDTO.class)).thenReturn(questionDTO2);
//
//        // Act
//        List<QuestionDTO> result = questionService.getAllManadatoryQuestions(categoryType);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals("What is your name?", result.get(0).getQuestion());
//        assertEquals("What is your age?", result.get(1).getQuestion());
//    }
//
//    @Test
//    void testGetAllManadatoryQuestions_noQuestionsFound() {
//        // Arrange
//        CategoryType categoryType = CategoryType.SPOT;
//
//        when(questionRepository.findAll()).thenReturn(List.of());
//
//        // Act & Assert
//        QuestionNotFoundException exception = assertThrows(QuestionNotFoundException.class, () -> {
//            questionService.getAllManadatoryQuestions(categoryType);
//        });
//        assertEquals("Question not found for type: SPOT", exception.getMessage());
//    }
//
//    @Test
//    void testGetAllManadatoryQuestions_withFilter() {
//        // Arrange
//        CategoryType categoryType = CategoryType.SPOT;
//        Question question1 = new Question(1L, "What is your name?", CategoryType.SPOT, DataType.BOOLEAN);
//        Question question2 = new Question(2L, "What is your age?", CategoryType.NOMINATION, DataType.BOOLEAN);
//
//    //    QuestionDTO questionDTO1 = new QuestionDTO(1L, "What is your name?", CategoryType.SPOT, DataType.BOOLEAN);
//        QuestionDTO questionDTO1 = QuestionDTO.builder()
//                .questionId(1L)
//                .question("What is your name?")
//                .type(CategoryType.SPOT)
//                .dataType("BOOLEAN")
//                .build();
//        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));
//        when(objectMapper.convertValue(question1, QuestionDTO.class)).thenReturn(questionDTO1);
//
//        // Act
//        List<QuestionDTO> result = questionService.getAllManadatoryQuestions(categoryType);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.get(0).getQuestionId());
//        assertEquals("What is your name?", result.get(0).getQuestion());
//    }
//
//}
