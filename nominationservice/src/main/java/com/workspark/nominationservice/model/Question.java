package com.workspark.nominationservice.model;



import com.workspark.nominationservice.enums.CategoryType;
import com.workspark.nominationservice.enums.DataType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(name = "question", nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CategoryType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private DataType dataType;


}































/*
 * import lombok.AllArgsConstructor; import lombok.Data; import
 * lombok.NoArgsConstructor; import jakarta.persistence.*; import
 * jakarta.validation.constraints.NotBlank; import
 * jakarta.validation.constraints.NotNull;
 * 
 * import java.time.LocalDateTime;
 * 
 * import org.hibernate.annotations.CreationTimestamp; import
 * org.hibernate.annotations.UpdateTimestamp;
 * 
 * import com.fasterxml.jackson.annotation.JsonInclude;
 * 
 * @Entity
 * 
 * @Data
 * 
 * @JsonInclude(JsonInclude.Include.NON_NULL)
 * 
 * @AllArgsConstructor
 * 
 * @NoArgsConstructor
 * 
 * @Table(name = "questions") public class Question {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long questionId;
 * 
 * @Column(nullable = false)
 * 
 * @NotBlank(message = "Question cannot be blank") private String question;
 * 
 * @Column(nullable = false)
 * 
 * @NotNull(message = "CreatedBy is required") private Long createdBy; // Tenant
 * Admin ID
 * 
 * @CreationTimestamp private LocalDateTime createdOn;
 * 
 * @UpdateTimestamp private LocalDateTime updatedOn; }
 */