package com.workspark.nominationservice.service;

import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.NominationRequestDTO;
import com.workspark.nominationservice.dto.NominationResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface NominationService {

    NominationResponseDTO createNomination(NominationRequestDTO requestDTO);

    NominationResponseDTO getNominationById(Long nominationId);

    NominationResponseDTO updateNomination(Long nominationId, NominationRequestDTO requestDTO);

    PaginatedRes<NominationResponseDTO> getAllNominations(int page, int size);
}
