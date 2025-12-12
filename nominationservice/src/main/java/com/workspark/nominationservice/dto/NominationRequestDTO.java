package com.workspark.nominationservice.dto;




import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class NominationRequestDTO {
    private Long categoryId;
    private String nomineeId;
    private Integer nominatedByUd;
    private List<QuestionAnswerDTO> questionAnswers;
}
