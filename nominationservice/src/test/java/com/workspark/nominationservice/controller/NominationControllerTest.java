//package com.workspark.nominationservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.workspark.nominationservice.dto.NominationRequestDTO;
//import com.workspark.nominationservice.dto.NominationResponseDTO;
//import com.workspark.nominationservice.service.NominationServiceImpl;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class NominationControllerTest {
//
//	private MockMvc mockMvc;
//
//	@Mock
//	private NominationServiceImpl nominationService;
//
//	@InjectMocks
//	private NominationController nominationController;
//
//	private NominationRequestDTO nominationRequestDTO;
//	private NominationResponseDTO nominationResponseDTO;
//
//	private final ObjectMapper objectMapper = new ObjectMapper();
//
//	@BeforeEach
//	void setUp() {
//		mockMvc = MockMvcBuilders.standaloneSetup(nominationController).build();
//
//		nominationRequestDTO = NominationRequestDTO.builder().categoryId(Long.valueOf(1)).nomineeName("John Doe")
//				.nomineeEmployeeId("E123").nomineeEmail("johndoe@example.com").nominatedByUserId(1001)
//				.questionAnswers(List.of()).build();
//
//		nominationResponseDTO = NominationResponseDTO.builder().id(1L).categoryId(Long.valueOf(1)).nomineeName("John Doe")
//				.nomineeEmployeeId("E123").nomineeEmail("johndoe@example.com").nominatedByUserId(1001)
//				.questionAnswers(List.of()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
//	}
//
//	@Test
//	void testCreateNomination_Success() throws Exception {
//		// Mock service behavior
//		when(nominationService.createNomination(any(NominationRequestDTO.class))).thenReturn(nominationResponseDTO);
//
//		mockMvc.perform(post("/api/v1/nomination").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(nominationRequestDTO))).andExpect(status().isCreated())
//				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.nomineeName").value("John Doe"))
//				.andExpect(jsonPath("$.nomineeEmail").value("johndoe@example.com"));
//	}
//
//	@Test
//	void testGetNominationById_Success() throws Exception {
//		// Mock service behavior
//		when(nominationService.getNominationById(1L)).thenReturn(nominationResponseDTO);
//
//		mockMvc.perform(get("/api/v1/nomination/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
//				.andExpect(jsonPath("$.nomineeName").value("John Doe"));
//	}
//
//	@Test
//	void testUpdateNomination_Success() throws Exception {
//		// Mock service behavior
//		when(nominationService.updateNomination(eq(1L), any(NominationRequestDTO.class)))
//				.thenReturn(nominationResponseDTO);
//
//		mockMvc.perform(put("/api/v1/nomination/1").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(nominationRequestDTO))).andExpect(status().isOk())
//				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.nomineeName").value("John Doe"));
//	}
//
//	@Test
//	void testGetAllNominations_Success() throws Exception {
//	    // Create a concrete Page object with data
//	    List<NominationResponseDTO> nominations = List.of(
//	            NominationResponseDTO.builder()
//	                .id(1L)
//	                .nomineeName("John Doe")
//	                .nomineeEmail("johndoe@example.com")
//	                .build(),
//	            NominationResponseDTO.builder()
//	                .id(2L)
//	                .nomineeName("Jane Smith")
//	                .nomineeEmail("janesmith@example.com")
//	                .build()
//	    );
//
//	    Pageable pageable = PageRequest.of(0, 10);
//	    PageImpl<NominationResponseDTO> pageResponse = new PageImpl<>(nominations, pageable, nominations.size());
//
//	    // Mock the service response
//	    when(nominationService.getAllNominations(0, 10)).thenReturn(pageResponse);
//
//	    // Perform the request
//	    mockMvc.perform(get("/api/v1/nomination")
//	                    .param("page", "0")
//	                    .param("size", "10"))
//	            .andExpect(status().isOk())
//	            .andExpect(jsonPath("$.content[0].id").value(1L))
//	            .andExpect(jsonPath("$.content[0].nomineeName").value("John Doe"))
//	            .andExpect(jsonPath("$.content[1].id").value(2L))
//	            .andExpect(jsonPath("$.content[1].nomineeName").value("Jane Smith"));
//	}
//}
