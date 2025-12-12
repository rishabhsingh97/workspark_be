/*
 * package com.workspark.service;
 * 
 * import static org.junit.jupiter.api.Assertions.assertEquals; import static
 * org.junit.jupiter.api.Assertions.assertNotNull; import static
 * org.junit.jupiter.api.Assertions.assertThrows; import static
 * org.junit.jupiter.api.Assertions.assertTrue; import static
 * org.mockito.ArgumentMatchers.any; import static org.mockito.Mockito.times;
 * import static org.mockito.Mockito.verify; import static
 * org.mockito.Mockito.when;
 * 
 * import java.time.LocalDateTime; import java.util.Arrays; import
 * java.util.Collections; import java.util.List; import java.util.Optional;
 * import java.util.stream.Collectors;
 * 
 * import org.junit.jupiter.api.Test; import
 * org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks;
 * import org.mockito.Mock; import org.mockito.junit.jupiter.MockitoExtension;
 * import org.springframework.data.domain.Page; import
 * org.springframework.data.domain.PageImpl; import
 * org.springframework.data.domain.Pageable;
 * 
 * import com.workspark.dto.NominationRequestDTO; import
 * com.workspark.dto.NominationResponseDTO; import
 * com.workspark.dto.QuestionAnswerDTO; import
 * com.workspark.exception.NominationNotFoundException; import
 * com.workspark.model.Nomination; import
 * com.workspark.model.NominationQuestionAnswerMapping; import
 * com.workspark.repository.NominationQuestionAnswerMappingRepository; import
 * com.workspark.repository.NominationRepository;
 * 
 * @ExtendWith(MockitoExtension.class) public class NominationServiceTest {
 * 
 * @Mock private NominationRepository nominationRepository;
 * 
 * @Mock private NominationQuestionAnswerMappingRepository mappingRepository;
 * 
 * @InjectMocks private NominationService nominationService;
 * 
 * private NominationRequestDTO createSampleRequestDTO() { QuestionAnswerDTO
 * questionAnswer = QuestionAnswerDTO.builder() .questionId(1)
 * .answer("Sample Answer") .build();
 * 
 * return NominationRequestDTO.builder() .categoryId(Long.valueOf(1))
 * .nomineeName("John Doe") .nomineeEmployeeId("EMP001")
 * .nomineeEmail("john.doe@example.com") .nominatedByUserId(2)
 * .questionAnswers(Collections.singletonList(questionAnswer)) .build(); }
 * 
 * private Nomination createSampleNomination() { Nomination nomination =
 * Nomination.builder() .id(1L) .categoryId(Long.valueOf(1))
 * .nomineeName("John Doe") .nomineeEmployeeId("EMP001")
 * .nomineeEmail("john.doe@example.com") .nominatedByUserId(2)
 * .createdAt(LocalDateTime.now()) .build();
 * 
 * NominationQuestionAnswerMapping mapping =
 * NominationQuestionAnswerMapping.builder() .questionId(1)
 * .answer("Sample Answer") .nomination(nomination) .build();
 * 
 * nomination.setQuestionAnswerMappings(Collections.singletonList(mapping));
 * return nomination; }
 * 
 * @Test public void testCreateNomination_Positive() { // Prepare
 * NominationRequestDTO requestDTO = createSampleRequestDTO(); Nomination
 * nomination = createSampleNomination();
 * 
 * // Mock
 * when(nominationRepository.save(any(Nomination.class))).thenReturn(nomination)
 * ;
 * 
 * // Execute NominationResponseDTO responseDTO =
 * nominationService.createNomination(requestDTO);
 * 
 * // Verify assertNotNull(responseDTO); assertEquals(nomination.getId(),
 * responseDTO.getId()); assertEquals(requestDTO.getNomineeName(),
 * responseDTO.getNomineeName()); assertEquals(requestDTO.getNomineeEmail(),
 * responseDTO.getNomineeEmail()); assertEquals(1,
 * responseDTO.getQuestionAnswers().size());
 * 
 * verify(nominationRepository, times(1)).save(any(Nomination.class)); }
 * 
 * @Test public void testGetNominationById_Positive() { // Prepare Long
 * nominationId = 1L; Nomination nomination = createSampleNomination();
 * 
 * // Mock
 * when(nominationRepository.findById(nominationId)).thenReturn(Optional.of(
 * nomination));
 * 
 * // Execute NominationResponseDTO responseDTO =
 * nominationService.getNominationById(nominationId);
 * 
 * // Verify assertNotNull(responseDTO); assertEquals(nomination.getId(),
 * responseDTO.getId()); assertEquals(nomination.getNomineeName(),
 * responseDTO.getNomineeName());
 * 
 * verify(nominationRepository, times(1)).findById(nominationId); }
 * 
 * @Test public void testGetNominationById_Negative_NotFound() { // Prepare Long
 * nonExistentId = 999L;
 * 
 * // Mock
 * when(nominationRepository.findById(nonExistentId)).thenReturn(Optional.empty(
 * ));
 * 
 * // Execute & Verify assertThrows(NominationNotFoundException.class, () -> {
 * nominationService.getNominationById(nonExistentId); });
 * 
 * verify(nominationRepository, times(1)).findById(nonExistentId); }
 * 
 * 
 * @Test public void testUpdateNomination_Positive() { // Prepare Long
 * nominationId = 1L; // Declare nominationId Nomination existingNomination =
 * createSampleNomination(); // Your method to create a sample Nomination
 * NominationRequestDTO updateDTO = createSampleRequestDTO(); // Your method to
 * create a sample RequestDTO
 * 
 * // Set updated values for the nomination
 * updateDTO.setNomineeName("Updated Name"); updateDTO.setCategoryId(2L);
 * updateDTO.setNomineeEmail("updated.email@example.com");
 * 
 * // Simulate the updated nomination based on the provided DTO //Nomination
 * updatedNomination = createSampleNomination();
 * 
 * // Create new question answer mappings based on the updated DTO
 * List<QuestionAnswerDTO> updatedQuestionAnswers = Arrays.asList(
 * QuestionAnswerDTO.builder().questionId(1).answer("Updated Answer 1").build(),
 * QuestionAnswerDTO.builder().questionId(2).answer("Updated Answer 2").build()
 * );
 * 
 * updateDTO.setQuestionAnswers(updatedQuestionAnswers); // Ensure DTO contains
 * question-answer mappings
 * 
 * // Create nomination question answer mappings for the updated nomination
 * List<NominationQuestionAnswerMapping> newMappings =
 * updatedQuestionAnswers.stream() .map(qa ->
 * NominationQuestionAnswerMapping.builder() .questionId(qa.getQuestionId())
 * .answer(qa.getAnswer()) .nomination(existingNomination) .build())
 * .collect(Collectors.toList());
 * 
 * existingNomination.setQuestionAnswerMappings(newMappings);
 * 
 * // Mock repository calls
 * when(nominationRepository.findById(nominationId)).thenReturn(Optional.of(
 * existingNomination));
 * when(nominationRepository.save(any(Nomination.class))).thenReturn(
 * existingNomination);
 * 
 * // Execute the update nomination service method NominationResponseDTO
 * responseDTO = nominationService.updateNomination(nominationId, updateDTO);
 * 
 * // Verify the response assertNotNull(responseDTO);
 * assertEquals(updateDTO.getNomineeName(), responseDTO.getNomineeName());
 * assertEquals(updateDTO.getCategoryId(), responseDTO.getCategoryId());
 * assertEquals(updateDTO.getNomineeEmail(), responseDTO.getNomineeEmail());
 * assertNotNull(responseDTO.getUpdatedAt()); // Ensure the updatedAt field is
 * not null assertEquals(updatedQuestionAnswers.size(),
 * responseDTO.getQuestionAnswers().size()); // Verify question-answer size
 * 
 * // Verify repository interactions verify(nominationRepository,
 * times(1)).findById(nominationId); verify(nominationRepository,
 * times(1)).save(any(Nomination.class)); }
 * 
 * 
 * 
 * 
 * @Test public void testUpdateNomination_Negative_NotFound() { // Prepare Long
 * nonExistentId = 999L; NominationRequestDTO updateDTO =
 * createSampleRequestDTO();
 * 
 * // Mock
 * when(nominationRepository.findById(nonExistentId)).thenReturn(Optional.empty(
 * ));
 * 
 * // Execute & Verify assertThrows(NominationNotFoundException.class, () -> {
 * nominationService.updateNomination(nonExistentId, updateDTO); });
 * 
 * verify(nominationRepository, times(1)).findById(nonExistentId); }
 * 
 * @Test public void testGetAllNominations_Positive() { // Prepare int page = 0;
 * int size = 10; Nomination nomination1 = createSampleNomination(); Nomination
 * nomination2 = createSampleNomination(); nomination2.setId(2L);
 * 
 * List<Nomination> nominations = Arrays.asList(nomination1, nomination2);
 * Page<Nomination> nominationPage = new PageImpl<>(nominations);
 * 
 * // Mock when(nominationRepository.findAll(any(Pageable.class))).thenReturn(
 * nominationPage);
 * 
 * // Execute Page<NominationResponseDTO> responsePage =
 * nominationService.getAllNominations(page, size);
 * 
 * // Verify assertNotNull(responsePage); assertEquals(2,
 * responsePage.getContent().size()); assertEquals(nomination1.getId(),
 * responsePage.getContent().get(0).getId()); assertEquals(nomination2.getId(),
 * responsePage.getContent().get(1).getId());
 * 
 * verify(nominationRepository, times(1)).findAll(any(Pageable.class)); }
 * 
 * @Test public void testGetAllNominations_Negative_EmptyPage() { // Prepare int
 * page = 0; int size = 10; Page<Nomination> emptyPage = new
 * PageImpl<>(Collections.emptyList());
 * 
 * // Mock
 * when(nominationRepository.findAll(any(Pageable.class))).thenReturn(emptyPage)
 * ;
 * 
 * // Execute Page<NominationResponseDTO> responsePage =
 * nominationService.getAllNominations(page, size);
 * 
 * // Verify assertNotNull(responsePage); assertTrue(responsePage.isEmpty());
 * 
 * verify(nominationRepository, times(1)).findAll(any(Pageable.class)); }
 * 
 * @Test public void testCreateNomination_Negative_EmptyQuestionAnswers() { //
 * Prepare NominationRequestDTO requestDTO = createSampleRequestDTO();
 * requestDTO.setQuestionAnswers(Collections.emptyList());
 * 
 * // Mock when(nominationRepository.save(any(Nomination.class))).thenReturn(
 * createSampleNomination());
 * 
 * // Execute NominationResponseDTO responseDTO =
 * nominationService.createNomination(requestDTO);
 * 
 * // Verify assertNotNull(responseDTO);
 * //assertTrue(responseDTO.getQuestionAnswers().isEmpty());
 * 
 * verify(nominationRepository, times(1)).save(any(Nomination.class)); } }
 */