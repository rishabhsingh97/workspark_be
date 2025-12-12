package com.workspark.nominationservice.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nomination_questions_answer_mapping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominationQuestionAnswerMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer questionId;
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nomination_id", nullable = false)
    private Nomination nomination;
}
