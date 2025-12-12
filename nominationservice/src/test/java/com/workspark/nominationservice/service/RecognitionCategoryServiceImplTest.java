//package com.workspark.nominationservice.service.serviceImpl;
//
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.workspark.nominationservice.dto.QuestionDTO;
//import com.workspark.nominationservice.dto.RecognitionCategoryRequestDTO;
//import com.workspark.nominationservice.dto.RecognitionCategoryResponseDTO;
//import com.workspark.nominationservice.enums.CategoryType;
//import com.workspark.nominationservice.enums.Constants;
//import com.workspark.nominationservice.enums.DataType;
//import com.workspark.nominationservice.exception.NoRecordsFoundException;
//import com.workspark.nominationservice.exception.RecognitionCategoryNotFoundException;
//import com.workspark.nominationservice.model.*;
//import com.workspark.nominationservice.repository.RecognitionCategoryRepository;
//import com.workspark.nominationservice.service.RecognitionCategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class RecognitionCategoryServiceImplTest {
//
//    @Mock
//    private RecognitionCategoryRepository recognitionCategoryRepository;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @InjectMocks
//    private RecognitionCategoryServiceImpl recognitionCategoryService;
//
//    private RecognitionCategory category;
//    private RecognitionCategory category2;
//    private RecognitionCategoryResponseDTO responseDTO;
//    private RecognitionCategoryResponseDTO responseDTO2;
//    private RecognitionCategoryRequestDTO requestDTO;
//
//    @BeforeEach
//    void setUp() {
//        QuestionDTO questionDTO1 = QuestionDTO.builder()
//                .questionId(1L)
//                .question("What is your name?")
//                .type(CategoryType.SPOT)
//                .dataType("BOOLEAN")
//                .build();
//        category = new RecognitionCategory();
//        category.setCategoryId(1L);
//        category.setCategoryName("Test Category");
//// Ensure that lists are initialized (if not done in entity constructor)
//        category.setCategoryNominatorsMappings(new ArrayList<>());
//        category.setCategoryPanelistMappings(new ArrayList<>());
//        category.setQuestionCategoryMappings(new ArrayList<>());
//
//        category2 = new RecognitionCategory();
//        category2.setCategoryId(2L);
//        category2.setCategoryName("Test Category2");
//// Ensure that lists are initialized (if not done in entity constructor)
//        category2.setCategoryNominatorsMappings(new ArrayList<>());
//        category2.setCategoryPanelistMappings(new ArrayList<>());
//        category2.setQuestionCategoryMappings(new ArrayList<>());
//        // Initializing responseDTO and responseDTO2
//            responseDTO = new RecognitionCategoryResponseDTO();
//            responseDTO.setCategoryId(1L);
//            responseDTO.setCategoryName("Test Category");
//        responseDTO.setQuestions(Collections.singletonList(questionDTO1));
//        responseDTO.setNominatedBy(Arrays.asList(101L, 102L));
//        responseDTO.setPanelistId(Arrays.asList(101L, 102L));
//
//            responseDTO2 = new RecognitionCategoryResponseDTO();
//            responseDTO2.setCategoryId(1L);
//            responseDTO2.setCategoryName("Test Category2");
//        responseDTO2.setQuestions(new ArrayList<>());
//        responseDTO2.setNominatedBy(new ArrayList<>());
//        responseDTO2.setPanelistId(new ArrayList<>());
//        requestDTO = new RecognitionCategoryRequestDTO();
//        // Using the builder pattern for QuestionDTO
//
//        requestDTO.setCategoryId(1L);
//        requestDTO.setCategoryName("Test Category");
//        requestDTO.setQuestions(Collections.singletonList(questionDTO1));
//        requestDTO.setNominatedBy(Arrays.asList(101L, 102L));
//        requestDTO.setPanelistId(Arrays.asList(101L, 102L));
//    }
//    @Test
//    public void testMapToResponseDTO() {
//
//        RecognitionCategory category = new RecognitionCategory();
//        category.setCategoryId(1L);
//        category.setCategoryName("Test Category");
//
//        // Prepare mappings for Nominators, Panelists, and Questions
//        category.setCategoryNominatorsMappings(createCategoryNominatorsMappings());
//        category.setCategoryPanelistMappings(createCategoryPanelistMappings());
//        category.setQuestionCategoryMappings(createQuestionCategoryMappings());
//
//        // Arrange: Prepare the expected result
//        RecognitionCategoryResponseDTO expectedResponseDTO = new RecognitionCategoryResponseDTO();
//        expectedResponseDTO.setCategoryId(1L);
//        expectedResponseDTO.setCategoryName("Test Category");
//
//        // Mocking the ObjectMapper to return expected response for convertValue
//        when(objectMapper.convertValue(eq(category), eq(RecognitionCategoryResponseDTO.class)))
//                .thenReturn(expectedResponseDTO);
//
//        // Act: Call the method to test
//        RecognitionCategoryResponseDTO responseDTO = recognitionCategoryService.mapToResponseDTO(category);
//
//        // Assert: Validate that the response DTO is correctly mapped
//        assertNotNull(responseDTO);
//        assertEquals("Test Category", responseDTO.getCategoryName());
//        assertEquals(1L, responseDTO.getCategoryId());
//
//        // Validate the custom fields
//    }
//    private List<CategoryNominatorsMapping> createCategoryNominatorsMappings() {
//        CategoryNominatorsMapping mapping = new CategoryNominatorsMapping();
//        CategoryNominatorMappingId id = new CategoryNominatorMappingId(1L, 1L);
//        mapping.setId(id);
//        return Collections.singletonList(mapping);
//    }
//
//    private List<CategoryPanelistMapping> createCategoryPanelistMappings() {
//        CategoryPanelistMapping mapping = new CategoryPanelistMapping();
//        CategoryPanelistMappingId id = new CategoryPanelistMappingId(2L, 1L);
//        mapping.setId(id);
//        return Collections.singletonList(mapping);
//    }
//
//    private List<QuestionCategoryMapping> createQuestionCategoryMappings() {
//        QuestionCategoryMapping mapping = new QuestionCategoryMapping();
//        mapping.setQuestionId(1L);
//        mapping.setQuestion("Test Question");
//        return Collections.singletonList(mapping);
//    }
//    @Test
//    public void testMapToQuestionCategoryMappingResponseDTO() {
//        // Arrange: Prepare a sample QuestionCategoryMapping entity
//        QuestionCategoryMapping mapping = new QuestionCategoryMapping();
//        mapping.setQuestion("Sample Question");
//        mapping.setQuestionId(123L);
//
//        QuestionDTO expectedQuestionDTO = new QuestionDTO();
//        expectedQuestionDTO.setQuestion("Sample Question");
//
//        // Mock ObjectMapper behavior
//        when(objectMapper.convertValue(mapping, QuestionDTO.class)).thenReturn(expectedQuestionDTO);
//
//        // Act: Call the private method using reflection
//        QuestionDTO actualQuestionDTO = recognitionCategoryService.mapToQuestionCategoryMappingResponseDTO(mapping);
//
//        // Assert: Validate the result
//        assertNotNull(actualQuestionDTO, "The mapped QuestionDTO should not be null.");
//        assertEquals("Sample Question", actualQuestionDTO.getQuestion(), "The question content should match.");
//        verify(objectMapper, times(1)).convertValue(mapping, QuestionDTO.class);
//    }
//    @Test
//    public void testCreateOrUpdateRecognitionCategory_updateExistingCategory() {
//        // Arrange: Prepare the DTO with updated sample data
//        RecognitionCategoryRequestDTO requestDTO = new RecognitionCategoryRequestDTO();
//        requestDTO.setCategoryId(1L);
//        requestDTO.setCategoryName("Updated Category Name");
//        requestDTO.setType(CategoryType.SPOT);
//        requestDTO.setNominatedBy(Arrays.asList(3L));
//        requestDTO.setPanelistId(Arrays.asList(4L));
//
//        // Mock the existing RecognitionCategory entity fetched from the repository
//        RecognitionCategory existingCategory = new RecognitionCategory();
//        existingCategory.setCategoryId(1L);
//        existingCategory.setCategoryName("Original Category Name");
//        existingCategory.setCategoryNominatorsMappings(new ArrayList<>(List.of(new CategoryNominatorsMapping())));
//        existingCategory.setCategoryPanelistMappings(new ArrayList<>(List.of(new CategoryPanelistMapping())));
//        existingCategory.setQuestionCategoryMappings(new ArrayList<>(List.of(new QuestionCategoryMapping())));
//
//        // Act: Call the method under test
//        // Updated entity after changes
//        RecognitionCategory updatedCategory = new RecognitionCategory();
//        updatedCategory.setCategoryId(1L);
//        updatedCategory.setCategoryName("Updated Category Name");
//        updatedCategory.setCategoryNominatorsMappings(createCategoryNominatorsMappings());
//        updatedCategory.setCategoryPanelistMappings(createCategoryPanelistMappings());
//        updatedCategory.setQuestionCategoryMappings(createQuestionCategoryMappings());
////        recognitionCategory.setCategoryNominatorsMappings(new ArrayList<>(List.of(new CategoryNominatorsMapping())));
////        recognitionCategory.setCategoryPanelistMappings(new ArrayList<>(List.of(new CategoryPanelistMapping())));
////        recognitionCategory.setQuestionCategoryMappings(new ArrayList<>(List.of(new QuestionCategoryMapping())));
//
//        // Act: Call the method under test
//        // Expected response DTO
//        RecognitionCategoryResponseDTO expectedResponseDTO = new RecognitionCategoryResponseDTO();
//        expectedResponseDTO.setCategoryId(1L);
//        expectedResponseDTO.setCategoryName("Updated Category Name");
//
//        QuestionDTO questionDTO = new QuestionDTO();
//        questionDTO.setQuestion("Test Question");
//
//        // Stubbing
//        when(recognitionCategoryRepository.findById(eq(1L))).thenReturn(Optional.of(existingCategory));
//        lenient().when(objectMapper.convertValue(eq(requestDTO), eq(RecognitionCategory.class))).thenReturn(updatedCategory);
//        when(objectMapper.convertValue(eq(updatedCategory), eq(RecognitionCategoryResponseDTO.class))).thenReturn(expectedResponseDTO);
//        lenient().when(objectMapper.convertValue(any(QuestionCategoryMapping.class), eq(QuestionDTO.class))).thenReturn(questionDTO);
//        when(recognitionCategoryRepository.save(any(RecognitionCategory.class))).thenReturn(updatedCategory);
//
//        // Act
//        RecognitionCategoryResponseDTO actualResponseDTO = recognitionCategoryService.createOrUpdateRecognitionCategory(requestDTO);
//
//        // Assert
//        assertNotNull(actualResponseDTO, "Response DTO should not be null.");
//        assertEquals(1L, actualResponseDTO.getCategoryId(), "Category ID should match the updated entity.");
//        assertEquals("Updated Category Name", actualResponseDTO.getCategoryName(), "Category name should match the updated entity.");
//
//        // Verify the repository interactions
//        verify(recognitionCategoryRepository, times(1)).findById(eq(1L));
//        verify(recognitionCategoryRepository, times(1)).save(any(RecognitionCategory.class));
//    }
//
//
//    @Test
//public void testClearExistingMappings_whenMappingsAreNonEmpty() {
//    // Arrange: Initialize mutable non-empty lists
//    RecognitionCategory recognitionCategory = new RecognitionCategory();
//
//    recognitionCategory.setCategoryNominatorsMappings(new ArrayList<>(List.of(new CategoryNominatorsMapping())));
//    recognitionCategory.setCategoryPanelistMappings(new ArrayList<>(List.of(new CategoryPanelistMapping())));
//    recognitionCategory.setQuestionCategoryMappings(new ArrayList<>(List.of(new QuestionCategoryMapping())));
//
//    // Act: Call the method under test
//    recognitionCategoryService.clearExistingMappings(recognitionCategory);
//
//    // Assert: Verify that lists are cleared (size should be zero)
//    assertTrue(recognitionCategory.getCategoryNominatorsMappings().isEmpty(), "Category Nominators Mappings should be cleared.");
//    assertTrue(recognitionCategory.getCategoryPanelistMappings().isEmpty(), "Category Panelist Mappings should be cleared.");
//    assertTrue(recognitionCategory.getQuestionCategoryMappings().isEmpty(), "Question Category Mappings should be cleared.");
//}
//
//    @Test
//    public void testClearExistingMappings_whenMappingsAreNull() {
//        // Arrange: Ensure that all mappings are null
//        RecognitionCategory recognitionCategory = new RecognitionCategory();
//        recognitionCategory.setCategoryNominatorsMappings(null);
//        recognitionCategory.setCategoryPanelistMappings(null);
//        recognitionCategory.setQuestionCategoryMappings(null);
//
//        // Act: Call the method under test
//        recognitionCategoryService.clearExistingMappings(recognitionCategory);
//
//        // Assert: Verify that lists are initialized as empty ArrayLists
//        assertNotNull(recognitionCategory.getCategoryNominatorsMappings(), "Category Nominators Mappings should not be null.");
//        assertNotNull(recognitionCategory.getCategoryPanelistMappings(), "Category Panelist Mappings should not be null.");
//        assertNotNull(recognitionCategory.getQuestionCategoryMappings(), "Question Category Mappings should not be null.");
//
//        assertTrue(recognitionCategory.getCategoryNominatorsMappings().isEmpty(), "Category Nominators Mappings should be empty.");
//        assertTrue(recognitionCategory.getCategoryPanelistMappings().isEmpty(), "Category Panelist Mappings should be empty.");
//        assertTrue(recognitionCategory.getQuestionCategoryMappings().isEmpty(), "Question Category Mappings should be empty.");
//    }
//
//    @Test
//    public void testClearExistingMappings_whenMappingsAreAlreadyEmpty() {
//        // Arrange: Initialize empty lists
//        RecognitionCategory recognitionCategory = new RecognitionCategory();
//
//        recognitionCategory.setCategoryNominatorsMappings(new ArrayList<>());
//        recognitionCategory.setCategoryPanelistMappings(new ArrayList<>());
//        recognitionCategory.setQuestionCategoryMappings(new ArrayList<>());
//
//        // Act: Call the method under test
//        recognitionCategoryService.clearExistingMappings(recognitionCategory);
//
//        // Assert: Verify that lists remain empty
//        assertTrue(recognitionCategory.getCategoryNominatorsMappings().isEmpty(), "Category Nominators Mappings should remain empty.");
//        assertTrue(recognitionCategory.getCategoryPanelistMappings().isEmpty(), "Category Panelist Mappings should remain empty.");
//        assertTrue(recognitionCategory.getQuestionCategoryMappings().isEmpty(), "Question Category Mappings should remain empty.");
//    }
//
//    @Test
//    public void testCreateOrUpdateRecognitionCategory_createNewCategory() {
//        // Arrange: Prepare the DTO with sample data
//        RecognitionCategoryRequestDTO requestDTO = new RecognitionCategoryRequestDTO();
//        requestDTO.setCategoryName("Employee of the Month");
//        requestDTO.setType(CategoryType.SPOT);
//        requestDTO.setNominatedBy(Arrays.asList(1L));
//        requestDTO.setPanelistId(Arrays.asList(2L));
//
//        RecognitionCategory category = new RecognitionCategory();
//        category.setCategoryId(1L);
//        category.setCategoryName("Employee of the Month");
//        category.setCategoryNominatorsMappings(createCategoryNominatorsMappings());
//        category.setCategoryPanelistMappings(createCategoryPanelistMappings());
//        category.setQuestionCategoryMappings(createQuestionCategoryMappings());
//
//        RecognitionCategoryResponseDTO expectedResponseDTO = new RecognitionCategoryResponseDTO();
//        expectedResponseDTO.setCategoryId(1L);
//        expectedResponseDTO.setCategoryName("Employee of the Month");
//
//        QuestionDTO questionDTO = new QuestionDTO();
//        questionDTO.setQuestion("Test Question");
//
//        // Stubbing
//        lenient().when(objectMapper.convertValue(eq(requestDTO), eq(RecognitionCategory.class))).thenReturn(category);
//        when(objectMapper.convertValue(eq(category), eq(RecognitionCategoryResponseDTO.class))).thenReturn(expectedResponseDTO);
//        lenient().when(objectMapper.convertValue(any(QuestionCategoryMapping.class), eq(QuestionDTO.class))).thenReturn(questionDTO);
//        when(recognitionCategoryRepository.save(any(RecognitionCategory.class))).thenReturn(category);
//
//        // Act
//        RecognitionCategoryResponseDTO actualResponseDTO = recognitionCategoryService.createOrUpdateRecognitionCategory(requestDTO);
//
//        // Assert
//        assertNotNull(actualResponseDTO, "Response DTO should not be null.");
//        assertEquals(1L, actualResponseDTO.getCategoryId(), "Category ID should match the saved entity.");
//        assertEquals("Employee of the Month", actualResponseDTO.getCategoryName(), "Category name should match the saved entity.");
//
//        // Verify the repository save method
//        verify(recognitionCategoryRepository, times(1)).save(any(RecognitionCategory.class));
//    }
//
//
//
//
//
//    @Test
//    void testUpdateRecognitionCategory_NotFound() {
//        // Arrange
//        when(recognitionCategoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        RecognitionCategoryNotFoundException exception = assertThrows(
//                RecognitionCategoryNotFoundException.class,
//                () -> recognitionCategoryService.createOrUpdateRecognitionCategory(requestDTO)
//        );
//
//        assertEquals(Constants.CATEGORY_NOT_FOUND.getMessage(1L), exception.getMessage());
//        verify(recognitionCategoryRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testUpdateRecognitionCategory_ThrowsException() {
//        when(recognitionCategoryRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RecognitionCategoryNotFoundException exception = assertThrows(RecognitionCategoryNotFoundException.class,
//                () -> recognitionCategoryService.createOrUpdateRecognitionCategory(requestDTO));
//
//        assertTrue(exception.getMessage().contains("not found"));
//        verify(recognitionCategoryRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testMapDtoToEntity_shouldMapCategoryNameCorrectly() throws JsonMappingException {
//        // Arrange
//        RecognitionCategoryRequestDTO dto = new RecognitionCategoryRequestDTO();
//        dto.setCategoryName("Test Category");
//
//        category = new RecognitionCategory();
//        // Mock the objectMapper to simulate successful mapping for other fields
//        when(objectMapper.updateValue(any(RecognitionCategory.class), eq(dto))).thenReturn(category);
//
//        // Act
//        recognitionCategoryService.mapDtoToEntity(dto, category);
//
//        // Assert
//        assertEquals("Test Category", category.getCategoryName(), "The category name should be set correctly.");
//        verify(objectMapper, times(1)).updateValue(category, dto);
//    }
//
//
//    @Test
//    void testGetAllRecognitionCategories_Success() {
//        // Given: A list of recognition categories
//
//
//        when(recognitionCategoryRepository.findAll()).thenReturn(List.of(category,category2));
//
//
//        // Mock conversion for both categories
//        when(objectMapper.convertValue(category, RecognitionCategoryResponseDTO.class)).thenReturn(responseDTO);
//        when(objectMapper.convertValue(category2, RecognitionCategoryResponseDTO.class)).thenReturn(responseDTO2);
//
//        // When: Calling the service method
//        List<RecognitionCategoryResponseDTO> categories = recognitionCategoryService.getAllRecognitionCategories();
//
//        // Then: Assert the response
//        assertNotNull(categories);
//        assertEquals(2, categories.size());
//        assertEquals("Test Category", categories.get(0).getCategoryName());
//        assertEquals("Test Category2", categories.get(1).getCategoryName());
//        verify(recognitionCategoryRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetAllRecognitionCategories_NoCategoriesFound() {
//        // Given: No categories in the repository
//        when(recognitionCategoryRepository.findAll()).thenReturn(List.of());
//
//        // When: Calling the service method
//        Exception exception = assertThrows(NoRecordsFoundException.class, () -> {
//            recognitionCategoryService.getAllRecognitionCategories();
//        });
//
//        // Then: Assert that the exception is thrown with the correct message
//        assertEquals("No recognition categories found.", exception.getMessage());
//        verify(recognitionCategoryRepository, times(1)).findAll();
//    }
//    @Test
//    void testGetRecognitionCategory_Success() {
//        // Given: An existing category
//        Long categoryId = 1L;
//
//
//        // Mock the repository to return the existing category
//        when(recognitionCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Mock the conversion from entity to DTO
//
//        when(objectMapper.convertValue(any(RecognitionCategory.class), eq(RecognitionCategoryResponseDTO.class)))
//                .thenReturn(responseDTO);
//
//        // When: Calling the getRecognitionCategory method
//        RecognitionCategoryResponseDTO result = recognitionCategoryService.getRecognitionCategory(categoryId);
//
//        // Then: Assert the response
//        assertNotNull(result);
//        assertEquals(categoryId, result.getCategoryId());
//        assertEquals("Test Category", result.getCategoryName());
//        verify(recognitionCategoryRepository, times(1)).findById(categoryId);
//    }
//    @Test
//    void testGetRecognitionCategory_NotFound() {
//        // Given: A non-existing category ID
//        Long categoryId = 1L;
//
//        // Mock the repository to return empty
//        when(recognitionCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());
//
//        // When & Then: Calling the getRecognitionCategory method should throw RecognitionCategoryNotFoundException
//        assertThrows(RecognitionCategoryNotFoundException.class,
//                () -> recognitionCategoryService.getRecognitionCategory(categoryId));
//
//        verify(recognitionCategoryRepository, times(1)).findById(categoryId);
//    }
//
//}
