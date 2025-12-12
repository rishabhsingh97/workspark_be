package com.workspark.nominationservice.dto;




import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class QuestionAnswerDTO {
    private Integer questionId;
    private String answer;
}
