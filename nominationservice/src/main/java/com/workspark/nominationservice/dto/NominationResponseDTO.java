package com.workspark.nominationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominationResponseDTO implements Serializable {
    private Long id;
    private Long categoryId;
    private String nomineeName;
    private String nomineeEmployeeId;
    private String nomineeEmail;
    private Integer nominatedByUserId;
    private List<QuestionAnswerDTO> questionAnswers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Parameterized constructor (if needed)
    public NominationResponseDTO(Long id, String nomineeName) {
        this.id = id;
        this.nomineeName = nomineeName;
    }

}


	





